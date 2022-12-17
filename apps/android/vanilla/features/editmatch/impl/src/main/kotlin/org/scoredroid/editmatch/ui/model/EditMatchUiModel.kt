package org.scoredroid.editmatch.ui.model

sealed interface EditMatchUiModel {
    data class Content(
        val matchName: String,
        val teams: List<Team>,
    ) : EditMatchUiModel {
        data class Team(val name: String, val score: Int)
    }

    object Loading : EditMatchUiModel

    object MatchNotFound : EditMatchUiModel
}