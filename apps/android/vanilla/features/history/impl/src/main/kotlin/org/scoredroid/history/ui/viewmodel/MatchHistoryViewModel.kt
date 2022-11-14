package org.scoredroid.history.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MatchHistoryViewModel : ViewModel() {

    val matches: StateFlow<List<UiModel>> = MutableStateFlow(
        listOf(
            UiModel(
                matchName = "first match",
                numberOfTeams = 3,
            ),
            UiModel(
                matchName = "second match",
                numberOfTeams = 1,
            )
        )
    )

    data class UiModel(
        val matchName: String,
        val numberOfTeams: Int,
    )
}