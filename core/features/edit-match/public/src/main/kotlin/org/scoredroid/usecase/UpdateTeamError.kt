package org.scoredroid.usecase

sealed class UpdateTeamError : Throwable() {
    data object MatchNotFound : UpdateTeamError()
    data object TeamNotFound : UpdateTeamError()
}
