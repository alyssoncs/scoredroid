package org.scoredroid.play.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.scoredroid.play.ui.viewmodel.PlayViewModel

@Composable
fun PlayScreen(
    viewModel: PlayViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    Button(onClick = {}, modifier) {
        Text(text = "$uiState")
    }
}
