package org.scoredroid.history.ui.screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.scoredroid.history.impl.R
import org.scoredroid.history.ui.model.MatchHistoryUiModel
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel
import org.scoredroid.ui.theme.ScoredroidTheme
import org.scoredroid.ui.tooling.PreviewThemes

@Composable
fun MatchHistoryScreen(
    viewModel: MatchHistoryViewModel,
    onMatchClick: (matchId: Long) -> Unit,
    onCreateMatchClick: () -> Unit,
    onEditMatchClick: (matchId: Long) -> Unit,
) {
    val uiModel by viewModel.uiState.collectAsState()

    MatchHistoryScreenContent(uiModel, onCreateMatchClick, onMatchClick, onEditMatchClick)
}

@Composable
private fun MatchHistoryScreenContent(
    uiModel: MatchHistoryUiModel,
    onCreateMatchClick: () -> Unit,
    onMatchClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
) {
    Scaffold(
        floatingActionButton = { Fab(uiModel, onCreateMatchClick) },
    ) { paddingValue ->
        when (uiModel) {
            is MatchHistoryUiModel.Content -> {
                if (uiModel.matches.isEmpty()) {
                    EmptyState(Modifier.padding(paddingValue))
                } else {
                    Matches(
                        uiModel.matches.toImmutableList(),
                        onMatchClick,
                        onEditClick,
                        Modifier.padding(paddingValue),
                    )
                }
            }

            MatchHistoryUiModel.Loading -> {
                Loading()
            }
        }
    }
}

@Composable
private fun Fab(
    uiModel: MatchHistoryUiModel,
    onCreateMatchClick: () -> Unit,
) {
    if (uiModel is MatchHistoryUiModel.Content) {
        FloatingActionButton(onClick = onCreateMatchClick) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.create_match_fab_content_description),
            )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Matches(
    matches: ImmutableList<MatchHistoryUiModel.Content.Match>,
    onMatchClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        items(
            items = matches,
            key = { match -> match.id },
        ) { match ->
            MatchItem(
                match,
                onMatchClick,
                onEditClick,
                Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    ),
                ),
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.no_matches),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Composable
private fun MatchItem(
    match: MatchHistoryUiModel.Content.Match,
    onMatchClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClickLabel = stringResource(id = R.string.match_tile_click_label)) {
                onMatchClick(match.id)
            },
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = match.matchName,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(text = formatNumberOfTeams(match.numberOfTeams))
            }
            IconButton(
                onClick = { onEditClick(match.id) },
            ) {
                Icon(
                    painterResource(id = R.drawable.edit_24),
                    contentDescription = stringResource(R.string.edit_match_button_content_description),
                )
            }
            IconButton(
                onClick = match.onRemove,
            ) {
                Icon(
                    painterResource(id = R.drawable.delete_24),
                    contentDescription = stringResource(R.string.delete_match_button_content_description),
                )
            }
        }
    }
}

@Composable
private fun formatNumberOfTeams(numberOfTeams: Int): String {
    return pluralStringResource(
        R.plurals.number_of_teams,
        numberOfTeams,
        numberOfTeams,
    )
}

@Composable
@PreviewThemes
private fun MatchHistoryScreenPreview() {
    ScoredroidTheme {
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
                ),
            ),
            onCreateMatchClick = {},
            onMatchClick = {},
        ) {}
    }
}

@Composable
@PreviewThemes
private fun MatchHistoryScreenEmptyStatePreview() {
    ScoredroidTheme {
        MatchHistoryScreenContent(
            uiModel = MatchHistoryUiModel.Content(emptyList()),
            onCreateMatchClick = {},
            onMatchClick = {},
        ) {}
    }
}

@Composable
@PreviewThemes
private fun MatchHistoryScreenLoadingPreview() {
    ScoredroidTheme {
        MatchHistoryScreenContent(
            uiModel = MatchHistoryUiModel.Loading,
            onCreateMatchClick = {},
            onMatchClick = {},
        ) {}
    }
}
