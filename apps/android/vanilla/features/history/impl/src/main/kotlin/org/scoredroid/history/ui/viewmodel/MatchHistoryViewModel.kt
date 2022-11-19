package org.scoredroid.history.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.match.domain.usecase.GetMatchesUseCase

class MatchHistoryViewModel(
    private val getMatches: GetMatchesUseCase,
) : ViewModel() {

    private val _matches: MutableStateFlow<List<UiModel>> = MutableStateFlow(emptyList())
    val matches: StateFlow<List<UiModel>> = _matches

    init {
        viewModelScope.launch {
            _matches.emit(getMatches().map(::toUiModel))
        }
    }

    private fun toUiModel(it: MatchResponse) = UiModel(
        matchName = it.name,
        numberOfTeams = it.teams.count()
    )

    data class UiModel(
        val matchName: String,
        val numberOfTeams: Int,
    )
}