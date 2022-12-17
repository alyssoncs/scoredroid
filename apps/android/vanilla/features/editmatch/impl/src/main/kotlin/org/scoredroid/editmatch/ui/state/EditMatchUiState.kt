package org.scoredroid.editmatch.ui.state

sealed interface EditMatchUiState {
    data class Content(
        val matchName: String,
        val teams: List<Team>,
    ) : EditMatchUiState {
        data class Team(val name: String, val score: Int)
    }

    object Loading : EditMatchUiState

    object MatchNotFound : EditMatchUiState
}