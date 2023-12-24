package org.scoredroid.domain.entities

import org.scoredroid.domain.entities.Score.Companion.toScore

data class Team(
    val name: String,
    val score: Score = 0.toScore(),
) {
    fun rename(name: String): Team {
        return copy(name = name)
    }

    fun updateScore(newScore: Score): Team {
        return copy(score = newScore)
    }
}
