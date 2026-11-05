package com.Huslehi3.Topzom.domain.usecase

import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.model.DailyGoalKey
import com.Huslehi3.Topzom.domain.model.DailyModifier
import com.Huslehi3.Topzom.domain.model.DailyState
import com.Huslehi3.Topzom.domain.model.RunResult
import com.Huslehi3.Topzom.domain.repository.GameRepository

class ResolveDailyChallengeUseCase(private val repository: GameRepository) {

    fun today(dayOfYear: Int): DailyState = repository.loadDaily(dayOfYear)

    fun modifierFor(dayOfYear: Int): DailyModifier =
        SampleData.modifiers[dayOfYear % SampleData.modifiers.size]

    operator fun invoke(dayOfYear: Int, result: RunResult): DailyState {
        val state = repository.loadDaily(dayOfYear)
        val goals = state.goals.map { goal ->
            if (goal.done) {
                goal
            } else {
                val reached = when (goal.key) {
                    DailyGoalKey.HITS -> result.hits >= goal.target
                    DailyGoalKey.STREAK -> result.bestStreak >= goal.target
                    DailyGoalKey.SCORE -> result.score >= goal.target
                }
                goal.copy(done = reached)
            }
        }
        val updated = state.copy(goals = goals)
        repository.saveDaily(updated)
        return updated
    }
}
