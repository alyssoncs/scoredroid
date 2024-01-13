package org.scoredroid.play.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProvider
import org.scoredroid.fragment.compose.composeView
import org.scoredroid.fragment.transactions.commitWithReordering
import org.scoredroid.play.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.play.ui.screen.PlayScreen
import org.scoredroid.play.ui.viewmodel.PlayViewModel
import org.scoredroid.ui.theme.ScoredroidTheme

internal class PlayFragment(
    vmFactory: ViewModelProvider.Factory,
    private val editMatchNavigationTargetProvider: EditMatchNavigationTargetProvider,
) : Fragment() {

    private val viewModel by viewModels<PlayViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val matchId = arguments?.getLong(MATCH_ID_NAV_ARG)
        require(matchId != null) {
            "${PlayFragment::class.simpleName} requires the match id as argument with the $MATCH_ID_NAV_ARG key"
        }

        return composeView {
            ScoredroidTheme {
                PlayScreen(viewModel = viewModel) {
                    val (fragment, args) = editMatchNavigationTargetProvider.getNavigationTarget(matchId)
                    parentFragmentManager.commitWithReordering {
                        replace(container?.id ?: 0, fragment, args, null)
                        addToBackStack(null)
                    }
                }
            }
        }
    }
}
