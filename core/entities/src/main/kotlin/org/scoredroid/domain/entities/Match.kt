package org.scoredroid.domain.entities

data class Match(
    val id: Long,
    val name: String,
    val teams: List<Team>,
) {
    fun containsTeam(teamAt: Int): Boolean {
        return teamAt in teams.indices
    }

    fun updateScore(
        teamAt: Int,
        newScore: Score,
    ): Match {
        if (!containsTeam(teamAt))
            throw IndexOutOfBoundsException(updateScoreErrorMessage(teamAt))

        return copy(
            teams = teams.mapIndexed { idx, team ->
                if (idx == teamAt) {
                    team.updateScore(newScore)
                } else {
                    team
                }
            },
        )
    }

    private fun updateScoreErrorMessage(teamAt: Int): String {
        fun matchIdentifier(): String = if (name.isNotBlank()) "named \"$name\"" else "with id $id"
        fun numberOfTeams(): String = "${if (teams.isEmpty()) "no" else "only ${teams.size}"} teams"

        return "Tried to update score of team with index $teamAt on match ${matchIdentifier()}" +
            ", but there are ${numberOfTeams()} in this match"
    }
}
