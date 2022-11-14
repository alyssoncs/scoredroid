package org.scoredroid.history.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel

@Composable
fun MatchHistoryScreen(viewModel: MatchHistoryViewModel) {
    val matches by viewModel.matches.collectAsState()

    MatchHistoryScreenContent(matches)
}

@Composable
private fun MatchHistoryScreenContent(
    matches: List<MatchHistoryViewModel.UiModel>
) {
    MaterialTheme {
        Column(Modifier.padding(16.dp)) {
            for (match in matches) {
                Text(text = match.matchName)
                Text(text = "${match.numberOfTeams} teams")
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MatchHistoryScreenPreview() {
    MatchHistoryScreenContent(
        matches = listOf(
            MatchHistoryViewModel.UiModel(
                matchName = "first match",
                numberOfTeams = 3,
            ),
            MatchHistoryViewModel.UiModel(
                matchName = "second match",
                numberOfTeams = 1,
            )
        )
    )
}
