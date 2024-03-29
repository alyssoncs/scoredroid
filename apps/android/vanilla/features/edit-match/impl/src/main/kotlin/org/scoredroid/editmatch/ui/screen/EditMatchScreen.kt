package org.scoredroid.editmatch.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import org.scoredroid.ui.components.Loading
import org.scoredroid.ui.theme.ScoredroidTheme
import org.scoredroid.ui.tooling.PreviewThemes

@Composable
internal fun EditMatchScreen(
    viewModel: EditMatchViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.shouldNavigateBack)
        onNavigateBack()

    EditMatchScreenContent(
        uiState = uiState,
    )
}

@Composable
private fun EditMatchScreenContent(
    uiState: EditMatchUiState,
) {
    Surface {
        when (uiState) {
            is EditMatchUiState.Content -> {
                EditMatchForm(
                    matchName = uiState.matchName,
                    teams = uiState.teams.map(EditMatchUiState.Content.Team::name)
                        .toImmutableList(),
                    saveButtonText = stringResource(id = R.string.save_match),
                    onMatchNameChange = uiState.onMatchNameChange,
                    onTeamNameChange = { idx, name ->
                        uiState.teams[idx].onNameChange(name)
                    },
                    onAddTeamClick = uiState.onAddTeam,
                    onRemoveTeamClick = { teamAt ->
                        uiState.teams[teamAt].onRemove()
                    },
                    onSaveClick = uiState.onSave,
                )
            }

            is EditMatchUiState.Loading -> Loading()
            is EditMatchUiState.MatchNotFound -> MatchNotFound()
        }
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
        )
    }
}

@Composable
@PreviewThemes
private fun EditMatchScreenLoadingPreview() {
    ScoredroidTheme {
        EditMatchScreenContent(
            uiState = EditMatchUiState.Loading(shouldNavigateBack = false),
        )
    }
}

@Composable
@PreviewThemes
private fun EditMatchScreenMatchNotFoundPreview() {
    ScoredroidTheme {
        EditMatchScreenContent(
            uiState = EditMatchUiState.MatchNotFound(shouldNavigateBack = false),
        )
    }
}
