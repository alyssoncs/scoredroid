package org.scoredroid.domain.entities

data class Team(
    val name: String,
    val score: Score,
) {
    fun rename(name: String): Team {
        return copy(name = name)
    }

    fun updateScore(newScore: Score): Team {
        return copy(score = newScore)
    }
}
