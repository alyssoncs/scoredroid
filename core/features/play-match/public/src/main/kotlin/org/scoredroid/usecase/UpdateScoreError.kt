package org.scoredroid.usecase

sealed class UpdateScoreError : Throwable() {
    data object MatchNotFound : UpdateScoreError()
    data object TeamNotFound : UpdateScoreError()
}
