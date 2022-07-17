package org.scoredroid.score.domain.usecase

sealed class UpdateScoreError : Throwable() {
    object MatchNotFound : UpdateScoreError()
    object TeamNotFound : UpdateScoreError()
}