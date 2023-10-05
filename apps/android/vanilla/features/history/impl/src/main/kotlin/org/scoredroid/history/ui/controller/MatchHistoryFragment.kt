package org.scoredroid.history.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.creatematch.ui.navigation.CreateMatchNavigationTargetProvider
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProvider
import org.scoredroid.fragment.transactions.commitWithReordering
import org.scoredroid.history.ui.screen.MatchHistoryScreen
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel
import org.scoredroid.play.ui.navigation.PlayNavigationTargetProvider

class MatchHistoryFragment(
    vmFactory: ViewModelProvider.Factory,
    private val editMatchNavigationTargetProvider: EditMatchNavigationTargetProvider,
    private val createMatchNavigationTargetProvider: CreateMatchNavigationTargetProvider,
    private val playNavigationTargetProvider: PlayNavigationTargetProvider,
) : Fragment() {

    private val viewModel by viewModels<MatchHistoryViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MatchHistoryScreen(
                    viewModel = viewModel,
                    onCreateMatchClick = { navigateToCreateMatchScreen(container) },
                    onMatchClick = { navigateToPlayScreen(it, container) },
                    onEditMatchClick = { navigateToEditMatchScreen(it, container) },
                )
            }
        }
    }

    private fun navigateToEditMatchScreen(
        matchId: Long,
        container: ViewGroup?,
    ) {
        parentFragmentManager.commitWithReordering {
            val (fragment, args) = editMatchNavigationTargetProvider.getNavigationTarget(matchId)
            replace(container?.id ?: 0, fragment, args, null)
            addToBackStack(null)
        }
    }

    private fun navigateToPlayScreen(
        matchId: Long,
        container: ViewGroup?,
    ) {
        parentFragmentManager.commitWithReordering {
            val (fragment, args) = playNavigationTargetProvider.getNavigationTarget(matchId)
            replace(container?.id ?: 0, fragment, args, null)
            addToBackStack(null)
        }
    }

    private fun navigateToCreateMatchScreen(
        container: ViewGroup?,
    ) {
        parentFragmentManager.commitWithReordering {
            val fragment = createMatchNavigationTargetProvider.getNavigationTarget()
            replace(container?.id ?: 0, fragment, null)
            addToBackStack(null)
        }
    }
}
