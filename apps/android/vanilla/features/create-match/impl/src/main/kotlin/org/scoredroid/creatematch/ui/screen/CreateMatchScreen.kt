package org.scoredroid.creatematch.ui.screen

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import org.scoredroid.creatematch.impl.R
import org.scoredroid.creatematch.ui.state.CreateMatchUiState
import org.scoredroid.creatematch.ui.viewmodel.CreateMatchViewModel
import org.scoredroid.ui.components.EditMatchForm
import org.scoredroid.ui.theme.ScoredroidTheme

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
                teams = uiState.teams,
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
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
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
