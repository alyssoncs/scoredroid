package org.scoredroid.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun EditMatchForm(
    matchName: String,
    teams: List<String>,
    saveButtonText: String,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Column {
        EditMatchFormContent(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f),
            machName = matchName,
            teams = teams.toImmutableList(),
            onMatchNameChange = onMatchNameChange,
            onTeamNameChange = onTeamNameChange,
            onAddTeamClick = onAddTeamClick,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            onClick = onSaveClick,
        ) {
            Text(saveButtonText)
        }
    }
}

@Composable
private fun EditMatchFormContent(
    machName: String,
    teams: ImmutableList<String>,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        item {
            MatchName(matchName = machName, onMatchNameChange = onMatchNameChange)
        }
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
    EditMatchForm(
        matchName = "Ultimate match",
        teams = listOf(
            "Champions",
            "Losers",
        ),
        saveButtonText = "Save",
        onMatchNameChange = {},
        onTeamNameChange = { _, _ -> },
        onAddTeamClick = {},
        onSaveClick = {},
    )
}
