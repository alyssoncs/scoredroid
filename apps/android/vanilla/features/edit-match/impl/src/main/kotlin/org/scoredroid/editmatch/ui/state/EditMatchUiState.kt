package org.scoredroid.editmatch.ui.state

sealed interface EditMatchUiState {

    val shouldNavigateBack: Boolean

    data class Content(
        val matchName: String,
        val teams: List<Team>,
        override val shouldNavigateBack: Boolean,
    ) : EditMatchUiState {
        data class Team(val name: String, val score: Int)
    }

    data class Loading(override val shouldNavigateBack: Boolean) : EditMatchUiState

    data class MatchNotFound(override val shouldNavigateBack: Boolean) : EditMatchUiState
}
