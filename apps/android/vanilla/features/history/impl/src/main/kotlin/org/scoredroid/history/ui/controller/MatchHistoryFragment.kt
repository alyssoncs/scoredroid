package org.scoredroid.history.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProvider
import org.scoredroid.history.ui.screen.MatchHistoryScreen
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel

class MatchHistoryFragment(
    vmFactory: ViewModelProvider.Factory,
    private val editMatchNavigationTargetProvider: EditMatchNavigationTargetProvider
) : Fragment() {

    private val viewModel by viewModels<MatchHistoryViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToEditScreen.collect {
                    it?.let { navigation ->
                        viewModel.onNavigateToEditScreen()
                        navigateToEditMatchScreen(navigation, container)
                    }
                }
            }
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MatchHistoryScreen(viewModel = viewModel)
            }
        }
    }

    private fun navigateToEditMatchScreen(
        navigation: MatchHistoryViewModel.Navigation,
        container: ViewGroup?
    ) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            val (fragment, args) = editMatchNavigationTargetProvider.getNavigationTarget(navigation.matchId)
            replace(container?.id ?: 0, fragment, args, null)
            addToBackStack(null)
        }
    }
}

