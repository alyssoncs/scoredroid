package org.scoredroid.creatematch.ui.state

data class CreateMatchUiState(
    val matchName: String = "",
    val teams: List<Team> = emptyList(),
    val loading: Boolean = false,
    val created: Boolean = false,
    val onMatchNameChange: (name: String) -> Unit = {},
    val onAddTeam: () -> Unit = {},
    val onCreate: () -> Unit = {},
) {
    data class Team(
        val name: String = "",
        val onNameChange: (name: String) -> Unit = {},
    )
}
