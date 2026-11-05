package com.Huslehi3.Topzom.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Huslehi3.Topzom.core.config.DayProvider
import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.model.GamePhase
import com.Huslehi3.Topzom.domain.model.RouteKey
import com.Huslehi3.Topzom.domain.model.RunResult
import com.Huslehi3.Topzom.domain.model.Target
import com.Huslehi3.Topzom.domain.model.TargetKind
import com.Huslehi3.Topzom.domain.repository.GameRepository
import com.Huslehi3.Topzom.domain.repository.SettingsRepository
import com.Huslehi3.Topzom.domain.usecase.AdvanceRoundUseCase
import com.Huslehi3.Topzom.domain.usecase.LaunchCoinUseCase
import com.Huslehi3.Topzom.domain.usecase.ResolveDailyChallengeUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

class GameViewModel(
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository,
    private val advanceRound: AdvanceRoundUseCase,
    private val resolveDaily: ResolveDailyChallengeUseCase,
    private val launchCoin: LaunchCoinUseCase,
    private val challenge: Boolean
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState(challenge = challenge))
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<RunResult>(Channel.BUFFERED)
    val events: Flow<RunResult> = _events.receiveAsFlow()

    private val random = Random(System.currentTimeMillis())

    private var boardWidth = 0f
    private var boardHeight = 0f
    private var loop: Job? = null
    private var clock = 0L
    private var phaseStart = 0L
    private var coinElapsed = 0L
    private var nextTargetId = 0
    private val respawnAt = HashMap<Int, Long>()
    private var speedFactor = 1f
    private var scoreFactor = 1
    private var bonusChance = GameConfig.BONUS_CHANCE
    private var hitRadius = GameConfig.HIT_RADIUS_DP
    private var driftBoost = 1f
    private var baseNodes = 0
    private var finished = false

    init {
        viewModelScope.launch {
            val profile = gameRepository.loadProfile()
            val settings = settingsRepository.load()
            hitRadius = if (settings.aimAssist) {
                GameConfig.AIM_ASSIST_RADIUS_DP
            } else {
                GameConfig.HIT_RADIUS_DP
            }
            val route = highestRoute(profile.unlockedRoutes)
            driftBoost = SampleData.routeOf(route).driftBoost
            if (challenge) {
                val modifier = resolveDaily.modifierFor(DayProvider.currentDayOfYear())
                speedFactor = modifier.speedFactor
                scoreFactor = modifier.scoreFactor
                bonusChance = (GameConfig.BONUS_CHANCE * modifier.bonusChanceFactor).coerceAtMost(0.5f)
            }
            baseNodes = (profile.routeProgress / GameConfig.ROUTE_SEGMENT)
                .coerceAtMost(GameConfig.ROUTE_NODES)
            _state.update {
                it.copy(
                    bestScore = profile.bestScore,
                    routeKey = route,
                    routeNodesReached = baseNodes
                )
            }
        }
    }

    fun onBoardMeasured(width: Float, height: Float) {
        if (width <= 0f || height <= 0f) return
        val changed = abs(width - boardWidth) > 0.5f || abs(height - boardHeight) > 0.5f
        boardWidth = width
        boardHeight = height
        if (changed || _state.value.targets.isEmpty()) {
            _state.update { it.copy(targets = spawnAll(), boardReady = true) }
        }
        resume()
    }

    fun launch() {
        val current = _state.value
        if (!current.launchEnabled) return
        coinElapsed = 0L
        phaseStart = clock
        val angle = Math.toRadians(current.aimDegrees.toDouble())
        val startX = boardWidth / 2f + (sin(angle) * 28.0).toFloat()
        val startY = boardHeight - GameConfig.LAUNCHER_INSET_DP - (cos(angle) * 28.0).toFloat()
        _state.update {
            it.copy(
                phase = GamePhase.LAUNCHING,
                coin = CoinState(startX, startY, 0f),
                recoil = 1f
            )
        }
    }

    fun pause() {
        loop?.cancel()
        loop = null
        if (_state.value.phase != GamePhase.FINISHED) {
            _state.update { it.copy(phase = GamePhase.PAUSED) }
        }
    }

    fun resume() {
        if (finished) return
        if (_state.value.phase == GamePhase.PAUSED) {
            phaseStart = clock
            _state.update { it.copy(phase = GamePhase.READY) }
        }
        if (loop == null) {
            loop = viewModelScope.launch {
                while (isActive) {
                    delay(GameConfig.TICK_MS)
                    tick()
                }
            }
        }
    }

    private fun tick() {
        if (boardWidth <= 0f || boardHeight <= 0f) return
        val current = _state.value
        if (current.phase == GamePhase.PAUSED || current.phase == GamePhase.FINISHED) return
        clock += GameConfig.TICK_MS

        var next = current.copy(targets = moveTargets(current))

        next = when (next.phase) {
            GamePhase.READY -> next.copy(aimDegrees = sweepAngle())
            GamePhase.LAUNCHING -> advanceCoin(next)
            GamePhase.HIT -> if (clock - phaseStart >= GameConfig.HIT_FEEDBACK_MS) {
                next.copy(phase = GamePhase.READY, effectProgress = 0f, coin = null)
            } else {
                next.copy(
                    effectProgress = (clock - phaseStart).toFloat() / GameConfig.HIT_FEEDBACK_MS
                )
            }

            GamePhase.MISS -> if (clock - phaseStart >= GameConfig.MISS_FEEDBACK_MS) {
                if (next.coinsLeft <= 0) {
                    finishRun(next)
                    next.copy(phase = GamePhase.FINISHED, coin = null, effectProgress = 0f)
                } else {
                    next.copy(phase = GamePhase.READY, coin = null, effectProgress = 0f)
                }
            } else {
                next.copy(
                    effectProgress = (clock - phaseStart).toFloat() / GameConfig.MISS_FEEDBACK_MS
                )
            }

            else -> next
        }

        next = next.copy(recoil = (next.recoil - 0.09f).coerceAtLeast(0f))
        _state.value = next
    }

    private fun sweepAngle(): Float {
        val period = GameConfig.AIM_SWEEP_MS
        val position = (clock % period).toFloat() / period
        val triangle = if (position < 0.5f) position * 2f else (1f - position) * 2f
        return (triangle * 2f - 1f) * GameConfig.MAX_AIM_DEGREES
    }

    private fun moveTargets(current: GameUiState): List<Target> {
        val level = (current.hits / 6).coerceAtMost(GameConfig.MAX_LEVEL)
        val multiplier = (1f + GameConfig.LEVEL_SPEED_STEP * level) * driftBoost * speedFactor
        val step = GameConfig.TICK_MS / 1000f
        val half = GameConfig.TARGET_SIZE_DP / 2f
        return current.targets.map { target ->
            if (!target.alive) {
                val due = respawnAt[target.id] ?: 0L
                if (clock >= due) {
                    respawnAt.remove(target.id)
                    spawnForLane(target.lane, fromEdge = true)
                } else {
                    target
                }
            } else {
                var x = target.x + target.speed * multiplier * step
                if (target.speed > 0f && x > boardWidth + half) {
                    x = -half
                } else if (target.speed < 0f && x < -half) {
                    x = boardWidth + half
                }
                target.copy(x = x)
            }
        }
    }

    private fun advanceCoin(current: GameUiState): GameUiState {
        val coin = current.coin ?: return current.copy(phase = GamePhase.READY)
        coinElapsed += GameConfig.TICK_MS
        val angle = Math.toRadians(current.aimDegrees.toDouble())
        val speed = boardHeight / GameConfig.COIN_FLIGHT_MS.toFloat()
        val travel = speed * GameConfig.TICK_MS
        val x = coin.x + (sin(angle) * travel).toFloat()
        val y = coin.y - (cos(angle) * travel).toFloat()
        val rotation = coin.rotation + 540f * GameConfig.TICK_MS / GameConfig.COIN_FLIGHT_MS

        val struck = launchCoin(x, y, current.targets, hitRadius)
        if (struck != null) {
            return registerHit(current, struck, x, y)
        }
        val outside = coinElapsed >= GameConfig.COIN_FLIGHT_MS ||
            y < -GameConfig.TARGET_SIZE_DP ||
            x < -GameConfig.TARGET_SIZE_DP ||
            x > boardWidth + GameConfig.TARGET_SIZE_DP
        if (outside) {
            return registerMiss(current)
        }
        return current.copy(coin = CoinState(x, y, rotation))
    }

    private fun registerHit(
        current: GameUiState,
        struck: Target,
        x: Float,
        y: Float
    ): GameUiState {
        phaseStart = clock
        respawnAt[struck.id] = clock + GameConfig.TARGET_RESPAWN_MS
        val streak = current.streak + 1
        val multiplier = minOf(1f + GameConfig.STREAK_STEP * streak, GameConfig.MAX_MULTIPLIER)
        val bonus = if (struck.kind == TargetKind.BONUS) GameConfig.BONUS_FACTOR else 1
        val points = (GameConfig.BASE_POINTS * multiplier).roundToInt() * bonus * scoreFactor
        val hits = current.hits + 1
        return current.copy(
            phase = GamePhase.HIT,
            targets = current.targets.map { if (it.id == struck.id) it.copy(alive = false) else it },
            score = current.score + points,
            hits = hits,
            streak = streak,
            bestStreak = maxOf(current.bestStreak, streak),
            lastPoints = points,
            effectX = x,
            effectY = y,
            effectProgress = 0f,
            coin = null,
            routeFill = (hits % GameConfig.ROUTE_SEGMENT).toFloat() / GameConfig.ROUTE_SEGMENT,
            routeNodesReached = (baseNodes + hits / GameConfig.ROUTE_SEGMENT)
                .coerceAtMost(GameConfig.ROUTE_NODES),
            lane = struck.lane + 1
        )
    }

    private fun registerMiss(current: GameUiState): GameUiState {
        phaseStart = clock
        return current.copy(
            phase = GamePhase.MISS,
            streak = 0,
            coinsLeft = (current.coinsLeft - 1).coerceAtLeast(0),
            coin = null,
            effectProgress = 0f
        )
    }

    private fun finishRun(current: GameUiState) {
        if (finished) return
        finished = true
        loop?.cancel()
        loop = null
        val result = RunResult(
            score = current.score,
            hits = current.hits,
            bestStreak = current.bestStreak,
            challenge = challenge,
            routeProgress = current.routeNodesReached
        )
        viewModelScope.launch {
            advanceRound(result)
            if (challenge) {
                resolveDaily(DayProvider.currentDayOfYear(), result)
            }
            delay(GameConfig.FINISH_DELAY_MS)
            _events.send(result)
        }
    }

    private fun spawnAll(): List<Target> {
        respawnAt.clear()
        nextTargetId = 0
        return (0 until GameConfig.LANES).map { lane -> spawnForLane(lane, fromEdge = false) }
    }

    private fun spawnForLane(lane: Int, fromEdge: Boolean): Target {
        val direction = if (lane % 2 == 0) 1f else -1f
        val base = GameConfig.LANE_SPEEDS_DP[lane % GameConfig.LANE_SPEEDS_DP.size]
        val half = GameConfig.TARGET_SIZE_DP / 2f
        val x = when {
            fromEdge && direction > 0f -> -half
            fromEdge -> boardWidth + half
            else -> boardWidth * (0.25f + 0.25f * lane)
        }
        val kind = when {
            random.nextFloat() < bonusChance -> TargetKind.BONUS
            lane % 2 == 0 -> TargetKind.MEDALLION
            else -> TargetKind.CRATE
        }
        nextTargetId += 1
        return Target(
            id = nextTargetId,
            lane = lane,
            kind = kind,
            x = x,
            y = laneY(lane),
            speed = base * direction,
            alive = true
        )
    }

    private fun laneY(lane: Int): Float = boardHeight * (0.14f + 0.16f * lane)

    private fun highestRoute(unlocked: Set<RouteKey>): RouteKey =
        SampleData.routes.lastOrNull { unlocked.contains(it.key) }?.key ?: RouteKey.HARBOUR

    override fun onCleared() {
        loop?.cancel()
        loop = null
        super.onCleared()
    }
}
