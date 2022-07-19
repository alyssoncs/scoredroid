package org.scoredroid.teams.domain.usecase

sealed class UpdateTeamError : Throwable() {
    object MatchNotFound : UpdateTeamError()
    object TeamNotFound : UpdateTeamError()
}
