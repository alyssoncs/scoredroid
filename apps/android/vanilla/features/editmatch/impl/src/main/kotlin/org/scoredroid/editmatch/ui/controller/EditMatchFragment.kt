package org.scoredroid.editmatch.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.editmatch.ui.navigation.MATCH_ID_NAV_ARG
import org.scoredroid.editmatch.ui.screen.EditMatchScreen
import org.scoredroid.editmatch.ui.viewmodel.EditMatchViewModel

class EditMatchFragment(
    vmFactory: ViewModelProvider.Factory
) : Fragment() {

    private val viewModel by viewModels<EditMatchViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                EditMatchScreen(viewModel)
            }
        }
    }

    companion object {
        fun createArgs(matchId: Long) = bundleOf(MATCH_ID_NAV_ARG to matchId)
    }
}

