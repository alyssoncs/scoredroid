package org.scoredroid.creatematch.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.scoredroid.creatematch.impl.R
import org.scoredroid.creatematch.ui.state.CreateMatchUiState
import org.scoredroid.creatematch.ui.viewmodel.CreateMatchViewModel
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
            CreateMatch(
                uiState,
                onMatchNameChange,
                onTeamNameChange,
                onAddTeamClick,
                onCreateClick,
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
private fun CreateMatch(
    uiModel: CreateMatchUiState,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Column {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MatchName(uiModel.matchName, onMatchNameChange)
            Spacer(modifier = Modifier.size(4.dp))
            Teams(uiModel.teams.toImmutableList(), onTeamNameChange, onAddTeamClick)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            onClick = onSaveClick,
        ) {
            Text(stringResource(R.string.create_match))
        }
    }
}

@Composable
private fun MatchName(
    matchName: String,
    onMatchNameChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = matchName,
        label = { Text(stringResource(R.string.match_name_label)) },
        onValueChange = onMatchNameChange,
    )
}

@Composable
private fun Teams(
    teams: ImmutableList<String>,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(teams) { idx, team ->
            TeamItem(
                modifier = Modifier.fillParentMaxWidth(),
                teamName = team,
            ) { name -> onTeamNameChange(idx, name) }
        }
        item {
            AddTeamButton(Modifier.fillParentMaxWidth(), onAddTeamClick)
        }
    }
}

@Composable
private fun AddTeamButton(modifier: Modifier = Modifier, onAddTeamClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            onClick = onAddTeamClick,
        ) {
            Text(stringResource(id = R.string.add_team))
        }
    }
}

@Composable
private fun TeamItem(
    teamName: String,
    modifier: Modifier = Modifier,
    onTeamNameChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = teamName,
        label = { Text(stringResource(R.string.team_name_label)) },
        onValueChange = onTeamNameChange,
    )
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
