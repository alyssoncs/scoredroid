package org.scoredroid.infra.dataaccess.error

sealed class TeamOperationError : Throwable() {
    object MatchNotFound : TeamOperationError()
    object TeamNotFound : TeamOperationError()
}
