package org.scoredroid.history.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.match.domain.usecase.GetMatchesUseCase

class MatchHistoryViewModel(
    private val getMatches: GetMatchesUseCase,
) : ViewModel() {

    val uiModel: StateFlow<UiModel> = flow {
        emit(UiModel.Content(getMatches().map(::toUiModel)))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiModel.Loading,
    )


    private fun toUiModel(it: MatchResponse) = UiModel.Content.Match(
        matchName = it.name,
        numberOfTeams = it.teams.count(),
    )

    sealed class UiModel {
        data class Content(
            val matches: List<Match>,
        ) : UiModel() {
            data class Match(val matchName: String, val numberOfTeams: Int)
        }

        object Loading : UiModel()
    }
}