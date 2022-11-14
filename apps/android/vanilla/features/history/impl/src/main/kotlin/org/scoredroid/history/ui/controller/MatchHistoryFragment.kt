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
import org.scoredroid.history.ui.screen.MatchHistoryScreen
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel

class MatchHistoryFragment(
    vmFactory: ViewModelProvider.Factory
) : Fragment() {

    private val viewModel by viewModels<MatchHistoryViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MatchHistoryScreen(viewModel = viewModel)
            }
        }
    }
}

