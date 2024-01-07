package org.scoredroid.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.scoredroid.ui.theme.ScoredroidTheme
import org.scoredroid.ui.tooling.PreviewThemes

@Composable
fun EditMatchForm(
    matchName: String,
    teams: ImmutableList<String>,
    saveButtonText: String,
    onMatchNameChange: (String) -> Unit,
    onTeamNameChange: (idx: Int, name: String) -> Unit,
    onAddTeamClick: () -> Unit,
    onSaveClick: () -> Unit,
    onRemoveTeamClick: (teamAt: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        EditMatchFormContent(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f),
            machName = matchName,
            teams = teams,
            onMatchNameChange = onMatchNameChange,
            onTeamNameChange = onTeamNameChange,
            onAddTeamClick = onAddTeamClick,
            onRemoveTeamClick = onRemoveTeamClick,
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
    onRemoveTeamClick: (teamAt: Int) -> Unit,
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
                onDeleteTeamClick = { onRemoveTeamClick(idx) },
                onTeamNameChange = { name -> onTeamNameChange(idx, name) },
            )
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
    onTeamNameChange: (String) -> Unit,
    onDeleteTeamClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = teamName,
            label = { Text(stringResource(R.string.team_name_label)) },
            onValueChange = onTeamNameChange,
        )

        IconButton(onClick = onDeleteTeamClick) {
            Icon(
                painterResource(id = R.drawable.delete_24),
                contentDescription = stringResource(R.string.remove_team_button_content_description),
            )
        }
    }
}

@Composable
@PreviewThemes
private fun EditMatchFormPreview() {
    ScoredroidTheme {
        Surface {
            EditMatchForm(
                matchName = "Ultimate match",
                teams = persistentListOf(
                    "Champions",
                    "Losers",
                ),
                saveButtonText = "Save",
                onMatchNameChange = {},
                onTeamNameChange = { _, _ -> },
                onAddTeamClick = {},
                onRemoveTeamClick = {},
                onSaveClick = {},
            )
        }
    }
}
