package org.scoredroid.editmatch.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.toImmutableList
import org.scoredroid.editmatch.impl.R
import org.scoredroid.editmatch.ui.state.EditMatchUiState
import org.scoredroid.editmatch.ui.viewmodel.EditMatchViewModel
import org.scoredroid.ui.components.EditMatchForm
import org.scoredroid.ui.theme.ScoredroidTheme
import org.scoredroid.ui.tooling.PreviewThemes

@Composable
fun EditMatchScreen(
    viewModel: EditMatchViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.shouldNavigateBack)
        onNavigateBack()

    EditMatchScreenContent(
        uiState = uiState,
        onMatchNameChange = viewModel::onMatchNameChange,
        onTeamNameChange = viewModel::onTeamNameChange,
        onAddTeamClick = viewModel::onAddTeam,
        onSaveClick = viewModel::onSave,
    )
}

@Composable
private fun EditMatchScreenContent(
    uiState: EditMatchUiState,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (uiState) {
            is EditMatchUiState.Content -> {
                EditMatchForm(
                    matchName = uiState.matchName,
                    teams = uiState.teams.map(EditMatchUiState.Content.Team::name)
                        .toImmutableList(),
                    saveButtonText = stringResource(id = R.string.save_match),
                    onMatchNameChange = onMatchNameChange,
                    onTeamNameChange = onTeamNameChange,
                    onAddTeamClick = onAddTeamClick,
                    onSaveClick = onSaveClick,
                    modifier = modifier,
                )
            }

            is EditMatchUiState.Loading -> Loading(modifier)
            is EditMatchUiState.MatchNotFound -> MatchNotFound(modifier)
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MatchNotFound(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.match_not_found),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
@PreviewThemes
private fun EditMatchScreenPreview() {
    ScoredroidTheme {
        EditMatchScreenContent(
            uiState = EditMatchUiState.Content(
                matchName = "Ultimate match",
                teams = listOf(
                    EditMatchUiState.Content.Team(
                        name = "Champions",
                        score = 4,
                    ),
                    EditMatchUiState.Content.Team(
                        name = "Losers",
                        score = 2,
                    ),
                ),
                shouldNavigateBack = false,
            ),
            onMatchNameChange = {},
            onTeamNameChange = { _, _ -> },
            onAddTeamClick = {},
            onSaveClick = {},
        )
    }
}

@Composable
@PreviewThemes
private fun EditMatchScreenLoadingPreview() {
    ScoredroidTheme {
        EditMatchScreenContent(
            uiState = EditMatchUiState.Loading(shouldNavigateBack = false),
            onMatchNameChange = {},
            onTeamNameChange = { _, _ -> },
            onAddTeamClick = {},
            onSaveClick = {},
        )
    }
}

@Composable
@PreviewThemes
private fun EditMatchScreenMatchNotFoundPreview() {
    ScoredroidTheme {
        EditMatchScreenContent(
            uiState = EditMatchUiState.MatchNotFound(shouldNavigateBack = false),
            onMatchNameChange = {},
            onTeamNameChange = { _, _ -> },
            onAddTeamClick = {},
            onSaveClick = {},
        )
    }
}
