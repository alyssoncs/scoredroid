package org.scoredroid.creatematch.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.toImmutableList
import org.scoredroid.creatematch.impl.R
import org.scoredroid.creatematch.ui.state.CreateMatchUiState
import org.scoredroid.creatematch.ui.viewmodel.CreateMatchViewModel
import org.scoredroid.ui.components.EditMatchForm
import org.scoredroid.ui.theme.ScoredroidTheme
import org.scoredroid.ui.tooling.PreviewThemes

@Composable
fun CreateMatchScreen(
    viewModel: CreateMatchViewModel,
    onCreated: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.created) onCreated()

    CreateMatchScreenContent(
        uiState = uiState,
        onMatchNameChange = viewModel::onMatchNameChange,
        onTeamNameChange = viewModel::onTeamNameChange,
        onAddTeamClick = viewModel::onAddTeam,
        onCreateClick = viewModel::onCreate,
    )
}

@Composable
private fun CreateMatchScreenContent(
    uiState: CreateMatchUiState,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    ScoredroidTheme {
        Box {
            EditMatchForm(
                matchName = uiState.matchName,
                teams = uiState.teams.toImmutableList(),
                saveButtonText = stringResource(id = R.string.create_match),
                onMatchNameChange = onMatchNameChange,
                onTeamNameChange = onTeamNameChange,
                onAddTeamClick = onAddTeamClick,
                onSaveClick = onCreateClick,
            )
            if (uiState.loading) {
                Loading()
            }
        }
    }
}

@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@PreviewThemes
private fun EditMatchScreenPreview() {
    CreateMatchScreenContent(
        uiState = CreateMatchUiState(
            matchName = "Ultimate match",
            teams = listOf(
                "Champions",
                "Losers",
            ),
            loading = false,
            created = false,
        ),
        onMatchNameChange = {},
        onTeamNameChange = { _, _ -> },
        onAddTeamClick = {},
        onCreateClick = {},
    )
}
