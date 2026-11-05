package com.Huslehi3.Topzom.data.repository

import com.Huslehi3.Topzom.data.local.CoinRoutePreferences
import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.model.DailyState
import com.Huslehi3.Topzom.domain.model.PlayerProfile
import com.Huslehi3.Topzom.domain.model.RouteKey
import com.Huslehi3.Topzom.domain.model.TierKey
import com.Huslehi3.Topzom.domain.model.TokenKey
import com.Huslehi3.Topzom.domain.repository.GameRepository

class GameRepositoryImpl(
    private val prefs: CoinRoutePreferences
) : GameRepository {

    override fun loadProfile(): PlayerProfile = PlayerProfile(
        bestScore = prefs.int(CoinRoutePreferences.BEST_SCORE, 0),
        bestStreak = prefs.int(CoinRoutePreferences.BEST_STREAK, 0),
        totalHits = prefs.int(CoinRoutePreferences.TOTAL_HITS, 0),
        runs = prefs.int(CoinRoutePreferences.RUNS, 0),
        tokens = prefs.int(CoinRoutePreferences.TOKENS, 0),
        lifetimeTokens = prefs.int(CoinRoutePreferences.LIFETIME_TOKENS, 0),
        routeProgress = prefs.int(CoinRoutePreferences.ROUTE_PROGRESS, 0),
        unlockedRoutes = decodeRoutes(prefs.text(CoinRoutePreferences.UNLOCKED_ROUTES, RouteKey.HARBOUR.name)),
        unlockedTokens = decodeTokens(prefs.text(CoinRoutePreferences.UNLOCKED_TOKENS, TokenKey.BRASS.name)),
        selectedToken = decodeToken(prefs.text(CoinRoutePreferences.SELECTED_TOKEN, TokenKey.BRASS.name)),
        claimedTiers = decodeTiers(prefs.text(CoinRoutePreferences.CLAIMED_TIERS, ""))
    )

    override fun saveProfile(profile: PlayerProfile) {
        prefs.putInt(CoinRoutePreferences.BEST_SCORE, profile.bestScore)
        prefs.putInt(CoinRoutePreferences.BEST_STREAK, profile.bestStreak)
        prefs.putInt(CoinRoutePreferences.TOTAL_HITS, profile.totalHits)
        prefs.putInt(CoinRoutePreferences.RUNS, profile.runs)
        prefs.putInt(CoinRoutePreferences.TOKENS, profile.tokens)
        prefs.putInt(CoinRoutePreferences.LIFETIME_TOKENS, profile.lifetimeTokens)
        prefs.putInt(CoinRoutePreferences.ROUTE_PROGRESS, profile.routeProgress)
        prefs.putText(CoinRoutePreferences.UNLOCKED_ROUTES, profile.unlockedRoutes.joinToString(SEP) { it.name })
        prefs.putText(CoinRoutePreferences.UNLOCKED_TOKENS, profile.unlockedTokens.joinToString(SEP) { it.name })
        prefs.putText(CoinRoutePreferences.SELECTED_TOKEN, profile.selectedToken.name)
        prefs.putText(CoinRoutePreferences.CLAIMED_TIERS, profile.claimedTiers.joinToString(SEP) { it.name })
    }

    override fun selectToken(key: TokenKey): PlayerProfile {
        val profile = loadProfile()
        if (!profile.unlockedTokens.contains(key)) return profile
        val updated = profile.copy(selectedToken = key)
        saveProfile(updated)
        return updated
    }

    override fun claimTier(key: TierKey): PlayerProfile {
        val profile = loadProfile()
        val tier = SampleData.tierOf(key)
        val updated = profile.copy(
            claimedTiers = profile.claimedTiers + key,
            tokens = profile.tokens + tier.grant
        )
        saveProfile(updated)
        return updated
    }

    override fun resetProgress(): PlayerProfile {
        prefs.clearProgress()
        return loadProfile()
    }

    override fun loadDaily(dayOfYear: Int): DailyState {
        val storedDay = prefs.int(CoinRoutePreferences.DAILY_DATE, -1)
        val modifier = SampleData.modifiers[dayOfYear % SampleData.modifiers.size]
        val flags = if (storedDay == dayOfYear) {
            prefs.text(CoinRoutePreferences.DAILY_GOALS, "")
        } else {
            ""
        }
        val goals = SampleData.dailyGoals.mapIndexed { index, goal ->
            goal.copy(done = flags.getOrNull(index) == DONE_FLAG)
        }
        return DailyState(
            dayOfYear = dayOfYear,
            modifier = modifier,
            goals = goals,
            rewardTokens = SampleData.DAILY_REWARD_TOKENS
        )
    }

    override fun saveDaily(state: DailyState) {
        prefs.putInt(CoinRoutePreferences.DAILY_DATE, state.dayOfYear)
        prefs.putText(
            CoinRoutePreferences.DAILY_GOALS,
            state.goals.joinToString("") { if (it.done) DONE_FLAG.toString() else OPEN_FLAG.toString() }
        )
    }

    private fun decodeRoutes(raw: String): Set<RouteKey> {
        val values = raw.split(SEP).mapNotNull { name ->
            RouteKey.entries.firstOrNull { it.name == name }
        }.toSet()
        return if (values.isEmpty()) setOf(RouteKey.HARBOUR) else values
    }

    private fun decodeTokens(raw: String): Set<TokenKey> {
        val values = raw.split(SEP).mapNotNull { name ->
            TokenKey.entries.firstOrNull { it.name == name }
        }.toSet()
        return if (values.isEmpty()) setOf(TokenKey.BRASS) else values
    }

    private fun decodeToken(raw: String): TokenKey =
        TokenKey.entries.firstOrNull { it.name == raw } ?: TokenKey.BRASS

    private fun decodeTiers(raw: String): Set<TierKey> =
        raw.split(SEP).mapNotNull { name -> TierKey.entries.firstOrNull { it.name == name } }.toSet()

    private companion object {
        const val SEP = ","
        const val DONE_FLAG = '1'
        const val OPEN_FLAG = '0'
    }
}
