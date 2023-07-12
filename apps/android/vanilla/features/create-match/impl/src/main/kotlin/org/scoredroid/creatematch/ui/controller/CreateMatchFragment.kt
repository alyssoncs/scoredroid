package org.scoredroid.creatematch.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.creatematch.ui.screen.CreateMatchScreen
import org.scoredroid.creatematch.ui.viewmodel.CreateMatchViewModel

class CreateMatchFragment(
    vmFactory: ViewModelProvider.Factory,
) : Fragment() {

    private val viewModel by viewModels<CreateMatchViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CreateMatchScreen(viewModel) {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
}
