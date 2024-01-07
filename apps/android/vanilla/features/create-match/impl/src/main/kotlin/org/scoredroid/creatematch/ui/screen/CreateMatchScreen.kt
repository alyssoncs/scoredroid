package org.scoredroid.creatematch.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.toImmutableList
import org.scoredroid.creatematch.impl.R
import org.scoredroid.creatematch.ui.state.CreateMatchUiState
import org.scoredroid.creatematch.ui.viewmodel.CreateMatchViewModel
import org.scoredroid.ui.components.EditMatchForm
import org.scoredroid.ui.components.Loading
import org.scoredroid.ui.theme.ScoredroidTheme
import org.scoredroid.ui.tooling.PreviewThemes

@Composable
fun CreateMatchScreen(
    viewModel: CreateMatchViewModel,
    onCreated: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.created) onCreated()

    CreateMatchScreenContent(uiState = uiState)
}

@Composable
private fun CreateMatchScreenContent(
    uiState: CreateMatchUiState,
) {
    Surface {
        Box {
            EditMatchForm(
                matchName = uiState.matchName,
                teams = uiState.teams.map(CreateMatchUiState.Team::name).toImmutableList(),
                saveButtonText = stringResource(id = R.string.create_match),
                onMatchNameChange = uiState.onMatchNameChange,
                onTeamNameChange = { teamAt, name ->
                    uiState.teams[teamAt].onNameChange(name)
                },
                onRemoveTeamClick = { teamAt ->
                    uiState.teams[teamAt].onRemove()
                },
                onAddTeamClick = uiState.onAddTeam,
                onSaveClick = uiState.onCreate,
            )
            if (uiState.loading) {
                Loading()
            }
        }
    }
}

@Composable
@PreviewThemes
private fun EditMatchScreenPreview() {
    ScoredroidTheme {
        CreateMatchScreenContent(
            uiState = CreateMatchUiState(
                matchName = "Ultimate match",
                teams = listOf(
                    CreateMatchUiState.Team("Champions"),
                    CreateMatchUiState.Team("Losers"),
                ),
                loading = false,
                created = false,
            ),
        )
    }
}
