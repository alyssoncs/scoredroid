package org.scoredroid.history.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.scoredroid.history.impl.R
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel
import org.scoredroid.ui.theme.ScoredroidTheme

@Composable
fun MatchHistoryScreen(viewModel: MatchHistoryViewModel) {
    val uiModel by viewModel.uiModel.collectAsState()

    MatchHistoryScreenContent(uiModel, viewModel::onClick)
}

@Composable
private fun MatchHistoryScreenContent(
    uiModel: MatchHistoryUiModel,
    onClick: (Long) -> Unit,
) {
    ScoredroidTheme {
        when (uiModel) {
            is MatchHistoryUiModel.Content -> MatchHistory(uiModel, onClick)
            MatchHistoryUiModel.Loading -> Loading()
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
private fun MatchHistory(
    uiModel: MatchHistoryUiModel.Content,
    onClick: (Long) -> Unit,
) {
    if (uiModel.matches.isEmpty())
        EmptyState()
    else
        Matches(uiModel.matches, onClick)
}

@Composable
private fun Matches(
    matches: List<MatchHistoryUiModel.Content.Match>,
    onClick: (Long) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(matches) { match ->
            MatchItem(match, onClick)
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.no_matches),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun MatchItem(
    match: MatchHistoryUiModel.Content.Match,
    onClick: (Long) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(match.id) }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = match.matchName,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = formatNumberOfTeams(match.numberOfTeams))
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun formatNumberOfTeams(numberOfTeams: Int): String {
    return pluralStringResource(
        R.plurals.number_of_teams,
        numberOfTeams,
        numberOfTeams,
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun MatchHistoryScreenPreview() {
    MatchHistoryScreenContent(
        uiModel = MatchHistoryUiModel.Content(
            listOf(
                MatchHistoryUiModel.Content.Match(
                    matchName = "first match",
                    numberOfTeams = 3,
                    id = 1,
                ),
                MatchHistoryUiModel.Content.Match(
                    matchName = "second match",
                    numberOfTeams = 1,
                    id = 2,
                ),
                MatchHistoryUiModel.Content.Match(
                    matchName = "third match",
                    numberOfTeams = 0,
                    id = 3,
                ),
                MatchHistoryUiModel.Content.Match(
                    matchName = "I hope you don't mind, but this is a very long named match",
                    numberOfTeams = 0,
                    id = 4,
                ),
            )
        ),
        onClick = {},
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun MatchHistoryScreenEmptyStatePreview() {
    MatchHistoryScreenContent(
        uiModel = MatchHistoryUiModel.Content(emptyList()),
        onClick = {},
    )
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun MatchHistoryScreenLoadingPreview() {
    MatchHistoryScreenContent(
        uiModel = MatchHistoryUiModel.Loading,
        onClick = {},
    )
}
