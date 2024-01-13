package org.scoredroid.history.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.creatematch.ui.navigation.CreateMatchNavigationTargetProvider
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProvider
import org.scoredroid.fragment.compose.composeView
import org.scoredroid.fragment.transactions.commitWithReordering
import org.scoredroid.history.ui.screen.MatchHistoryScreen
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel
import org.scoredroid.play.ui.navigation.PlayNavigationTargetProvider
import org.scoredroid.ui.theme.ScoredroidTheme

internal class MatchHistoryFragment(
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
        return composeView {
            ScoredroidTheme {
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
        val (fragment, args) = editMatchNavigationTargetProvider.getNavigationTarget(matchId)
        navigate(container, fragment, args)
    }

    private fun navigateToPlayScreen(
        matchId: Long,
        container: ViewGroup?,
    ) {
        val (fragment, args) = playNavigationTargetProvider.getNavigationTarget(matchId)
        navigate(container, fragment, args)
    }

    private fun navigateToCreateMatchScreen(
        container: ViewGroup?,
    ) {
        val fragment = createMatchNavigationTargetProvider.getNavigationTarget()
        navigate(container, fragment)
    }

    private fun navigate(
        container: ViewGroup?,
        fragment: Class<out Fragment>,
        args: Bundle? = null,
    ) {
        parentFragmentManager.commitWithReordering {
            replace(container?.id ?: 0, fragment, args, null)
            addToBackStack(null)
        }
    }
}
