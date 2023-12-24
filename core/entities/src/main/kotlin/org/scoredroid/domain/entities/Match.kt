package org.scoredroid.domain.entities

data class Match(
    val id: Long,
    val name: String,
    val teams: List<Team>,
) {
    fun rename(name: String): Match {
        return copy(name = name)
    }

    fun addTeam(team: Team): Match {
        return copy(
            teams = teams + team,
        )
    }

    fun removeTeam(teamAt: Int): Match {
        if (!containsTeam(teamAt))
            throw IndexOutOfBoundsException(removeTeamErrorMessage(teamAt))

        return copy(
            teams = teams.filterIndexed { idx, _ -> idx != teamAt },
        )
    }

    fun moveTeam(
        teamAt: Int,
        moveTo: Int,
    ): Match {
        if (!containsTeam(teamAt))
            throw IndexOutOfBoundsException(moveTeamErrorMessage(teamAt))

        val teams = teams.toMutableList()
        val indexToMove = moveTo.coerceIn(teams.indices)
        val removed = teams.removeAt(teamAt)
        teams.add(indexToMove, removed)

        return copy(teams = teams.toList())
    }

    fun updateScore(
        teamAt: Int,
        newScore: Score,
    ): Match {
        if (!containsTeam(teamAt))
            throw IndexOutOfBoundsException(updateScoreErrorMessage(teamAt))

        return copy(
            teams = teams.mapIndexed { idx, team ->
                if (idx == teamAt)
                    team.updateScore(newScore)
                else
                    team
            },
        )
    }

    fun renameTeam(
        teamAt: Int,
        name: String,
    ): Match {
        if (!containsTeam(teamAt))
            throw IndexOutOfBoundsException(renameTeamErrorMessage(teamAt))

        return copy(
            teams = teams.mapIndexed { idx, team ->
                if (idx == teamAt) team.rename(name) else team
            },
        )
    }

    private fun containsTeam(teamAt: Int): Boolean {
        return teamAt in teams.indices
    }

    private fun removeTeamErrorMessage(teamAt: Int): String {
        return teamIndexNotFoundErrorMessage(teamAt, "Tried to remove")
    }

    private fun moveTeamErrorMessage(teamAt: Int): String {
        return teamIndexNotFoundErrorMessage(teamAt, "Tried to move")
    }

    private fun updateScoreErrorMessage(teamAt: Int): String {
        return teamIndexNotFoundErrorMessage(teamAt, "Tried to update score of")
    }

    private fun renameTeamErrorMessage(teamAt: Int): String {
        return teamIndexNotFoundErrorMessage(teamAt, "Tried to rename")
    }

    private fun teamIndexNotFoundErrorMessage(teamAt: Int, operation: String): String {
        fun matchIdentifier(): String = if (name.isNotBlank()) "named \"$name\"" else "with id $id"
        fun numberOfTeams(): String = "${if (teams.isEmpty()) "no" else "only ${teams.size}"} teams"

        return "$operation team with index $teamAt on match ${matchIdentifier()}" +
            ", but there are ${numberOfTeams()} in this match"
    }
}
