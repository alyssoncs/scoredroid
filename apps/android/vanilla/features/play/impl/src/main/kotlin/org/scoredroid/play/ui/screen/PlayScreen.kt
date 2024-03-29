package org.scoredroid.play.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.scoredroid.play.impl.R
import org.scoredroid.play.ui.state.PlayUiState
import org.scoredroid.play.ui.viewmodel.PlayViewModel
import org.scoredroid.ui.components.Loading
import org.scoredroid.ui.theme.ScoredroidTheme
import org.scoredroid.ui.tooling.PreviewThemes
import kotlin.text.Regex.Companion.escape

@Composable
internal fun PlayScreen(
    viewModel: PlayViewModel,
    modifier: Modifier = Modifier,
    onEditMatchClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    PlayScreenContent(
        uiState = uiState,
        onEditMatchClick = onEditMatchClick,
        modifier = modifier,
    )
}

@Composable
private fun PlayScreenContent(
    uiState: PlayUiState,
    onEditMatchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier) {
        when (uiState) {
            is PlayUiState.Content -> TeamList(uiState, onEditMatchClick)
            PlayUiState.Loading -> Loading()
            PlayUiState.Error -> MatchNotFound()
        }
    }
}

@Composable
private fun TeamList(
    uiState: PlayUiState.Content,
    onEditMatchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.teams.isEmpty()) {
        EmptyMatch(modifier, onEditMatchClick)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier,
        ) {
            items(items = uiState.teams) { team ->
                TeamTile(
                    team = team,
                )
            }
        }
    }
}

@Composable
private fun EmptyMatch(
    modifier: Modifier = Modifier,
    onEditMatchClick: () -> Unit,
) {
    val editMatchAnnotation = "edit_match"
    val annotatedString = buildEmptyMessage(editMatchAnnotation)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .clickable(onClick = onEditMatchClick)
                .padding(16.dp),
        ) { offset ->
            if (annotatedString.hasStringAnnotations(editMatchAnnotation, offset, offset)) {
                onEditMatchClick()
            }
        }
    }
}

@Composable
private fun buildEmptyMessage(linkAnnotation: String): AnnotatedString {
    val addTeamLink = stringResource(id = R.string.add_team_link)
    val message = stringResource(id = R.string.empty_match_message_template, addTeamLink)
    val linkOccurrences = escape(addTeamLink).toRegex().findAll(message)
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = LocalContentColor.current)) {
            append(message)
        }
        linkOccurrences.forEach { occurrence ->
            val start = occurrence.range.first
            val end = occurrence.range.last.inc()
            val linkStyle = SpanStyle(color = MaterialTheme.colorScheme.primary)
            addStyle(style = linkStyle, start = start, end = end)
            addStringAnnotation(tag = linkAnnotation, annotation = linkAnnotation, start = start, end = end)
        }
    }
    return annotatedString
}

@Composable
fun MatchNotFound(
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
private fun TeamTile(
    team: PlayUiState.Content.Team,
) {
    val accessibilityActions = listOf(
        decrementScoreAccessibilityAction(team.onDecrement),
        incrementScoreAccessibilityAction(team.onIncrement),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                customActions = accessibilityActions
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val noSemantics = Modifier.clearAndSetSemantics { }

            DecrementScoreButton(noSemantics, team.onDecrement)
            TeamInfo(team)
            IncrementScoreButton(noSemantics, team.onIncrement)
        }
    }
}

@Composable
private fun decrementScoreAccessibilityAction(decrementScore: () -> Unit): CustomAccessibilityAction {
    val decrementScoreActionLabel =
        stringResource(R.string.decrement_score_button_content_description)
    return CustomAccessibilityAction(decrementScoreActionLabel) {
        decrementScore()
        true
    }
}

@Composable
private fun incrementScoreAccessibilityAction(incrementScore: () -> Unit): CustomAccessibilityAction {
    val incrementScoreActionLabel =
        stringResource(R.string.increment_score_button_content_description)
    return CustomAccessibilityAction(incrementScoreActionLabel) {
        incrementScore()
        true
    }
}

@Composable
private fun DecrementScoreButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.round_remove_24),
            contentDescription = stringResource(id = R.string.decrement_score_button_content_description),
        )
    }
}

@Composable
private fun TeamInfo(team: PlayUiState.Content.Team) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = team.name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = pluralStringResource(
                id = R.plurals.score,
                count = team.score,
                team.score,
            ),
        )
    }
}

@Composable
private fun IncrementScoreButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            painterResource(id = R.drawable.round_add_24),
            contentDescription = stringResource(id = R.string.increment_score_button_content_description),
        )
    }
}

@PreviewThemes
@Composable
private fun PlayScreenPreview() {
    ScoredroidTheme {
        PlayScreenContent(
            uiState = PlayUiState.Content(
                matchName = "The match",
                teams = listOf(
                    PlayUiState.Content.Team(
                        name = "Team 01",
                        score = 20,
                    ),
                    PlayUiState.Content.Team(
                        name = "Team 02",
                        score = 16,
                    ),
                ),
            ),
            onEditMatchClick = {},
        )
    }
}

@PreviewThemes
@Composable
private fun EmptyPlayScreenPreview() {
    ScoredroidTheme {
        PlayScreenContent(
            uiState = PlayUiState.Content(
                matchName = "The match",
                teams = emptyList(),
            ),
            onEditMatchClick = {},
        )
    }
}

@PreviewThemes
@Composable
private fun PlayScreenLoadingPreview() {
    ScoredroidTheme {
        PlayScreenContent(
            uiState = PlayUiState.Loading,
            onEditMatchClick = {},
        )
    }
}
