package org.scoredroid.creatematch.ui.state

data class CreateMatchUiState(
    val matchName: String,
    val teams: List<String>,
    val loading: Boolean,
    val created: Boolean,
)
