package org.scoredroid.play.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.play.ui.screen.PlayScreen
import org.scoredroid.play.ui.viewmodel.PlayViewModel

class PlayFragment(
    vmFactory: ViewModelProvider.Factory,
) : Fragment() {

    private val viewModel by viewModels<PlayViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PlayScreen(viewModel)
            }
        }
    }
}
