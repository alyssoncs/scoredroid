package org.scoredroid.history.ui.model

sealed interface MatchHistoryUiModel {
    data class Content(
        val matches: List<Match>,
    ) : MatchHistoryUiModel {
        data class Match(
            val matchName: String,
            val numberOfTeams: Int,
            val id: Long,
            val onRemove: () -> Unit = {},
        )
    }

    data object Loading : MatchHistoryUiModel
}
