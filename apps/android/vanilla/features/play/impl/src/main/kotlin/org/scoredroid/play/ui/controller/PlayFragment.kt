package org.scoredroid.play.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.fragment.compose.composeView
import org.scoredroid.play.ui.screen.PlayScreen
import org.scoredroid.play.ui.viewmodel.PlayViewModel
import org.scoredroid.ui.theme.ScoredroidTheme

class PlayFragment(
    vmFactory: ViewModelProvider.Factory,
) : Fragment() {

    private val viewModel by viewModels<PlayViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return composeView {
            ScoredroidTheme {
                PlayScreen(viewModel)
            }
        }
    }
}
