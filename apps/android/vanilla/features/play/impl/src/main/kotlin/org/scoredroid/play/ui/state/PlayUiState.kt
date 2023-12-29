package org.scoredroid.play.ui.state

sealed interface PlayUiState {
    data object Error : PlayUiState
    data object Loading : PlayUiState
    data class Content(
        val matchName: String,
        val teams: List<Team>,
    ) : PlayUiState {
        data class Team(
            val name: String,
            val score: Int,
            val onIncrement: () -> Unit = {},
            val onDecrement: () -> Unit = {},
        )
    }
}
