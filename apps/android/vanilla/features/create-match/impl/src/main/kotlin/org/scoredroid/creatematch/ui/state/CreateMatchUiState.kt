package org.scoredroid.creatematch.ui.state

data class CreateMatchUiState(
    val matchName: String = "",
    val teams: List<String> = emptyList(),
    val loading: Boolean = false,
    val created: Boolean = false,
    val onMatchNameChange: (name: String) -> Unit = {},
    val onAddTeam: () -> Unit = {},
    val onTeamNameChange: (teamAt: Int, name: String) -> Unit = { _, _ -> },
    val onCreate: () -> Unit = {},
)
