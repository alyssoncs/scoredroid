package org.scoredroid.editmatch.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import org.scoredroid.editmatch.impl.R
import org.scoredroid.editmatch.ui.model.EditMatchUiModel
import org.scoredroid.editmatch.ui.viewmodel.EditMatchViewModel
import org.scoredroid.ui.theme.ScoredroidTheme

@Composable
fun EditMatchScreen(
    viewModel: EditMatchViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    EditMatchScreenContent(
        uiState = uiState,
        onMatchNameChange = viewModel::onMatchNameChange,
        onTeamNameChange = viewModel::onTeamNameChange,
        onAddTeamClick = viewModel::onAddTeam,
    )
}

@Composable
private fun EditMatchScreenContent(
    uiState: EditMatchUiModel,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
) {
    ScoredroidTheme {
        when (uiState) {
            is EditMatchUiModel.Content -> EditMatch(
                uiState,
                onMatchNameChange,
                onTeamNameChange,
                onAddTeamClick,
            )
            EditMatchUiModel.Loading -> Loading()
            EditMatchUiModel.MatchNotFound -> MatchNotFound()
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
fun MatchNotFound() {
    Column(
        modifier = Modifier.fillMaxSize(),
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
private fun EditMatch(
    uiModel: EditMatchUiModel.Content,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
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
            Teams(uiModel.teams, onTeamNameChange)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            onClick = onAddTeamClick,
        ) {
            Text(stringResource(R.string.save_match))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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
    teams: List<EditMatchUiModel.Content.Team>,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(teams) { idx, team ->
            TeamItem(
                modifier = Modifier.fillParentMaxWidth(),
                team = team
            ) { name -> onTeamNameChange(idx, name) }
        }
        item {
            AddTeamButton(Modifier.fillParentMaxWidth())
        }
    }
}

@Composable
private fun AddTeamButton(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {

        TextButton(
            onClick = { },
        ) {
            Text(stringResource(id = R.string.add_team))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TeamItem(
    modifier: Modifier,
    team: EditMatchUiModel.Content.Team,
    onTeamNameChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = team.name,
        label = { Text(stringResource(R.string.team_name_label)) },
        onValueChange = onTeamNameChange,
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun EditMatchScreenPreview() {
    EditMatchScreenContent(
        uiState = EditMatchUiModel.Content(
            matchName = "Ultimate match",
            teams = listOf(
                EditMatchUiModel.Content.Team(
                    name = "Champions",
                    score = 4,
                ),
                EditMatchUiModel.Content.Team(
                    name = "Losers",
                    score = 2,
                )
            ),
        ),
        onMatchNameChange = {},
        onTeamNameChange = { _, _ -> },
        onAddTeamClick = {}
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun EditMatchScreenLoadingPreview() {
    EditMatchScreenContent(
        uiState = EditMatchUiModel.Loading,
        onMatchNameChange = {},
        onTeamNameChange = { _, _ -> },
        onAddTeamClick = {}
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun EditMatchScreenMatchNotFoundPreview() {
    EditMatchScreenContent(
        uiState = EditMatchUiModel.MatchNotFound,
        onMatchNameChange = {},
        onTeamNameChange = { _, _ -> },
        onAddTeamClick = {}
    )
}
