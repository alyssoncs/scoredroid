package org.scoredroid.editmatch.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.scoredroid.editmatch.ui.screen.EditMatchScreen
import org.scoredroid.editmatch.ui.viewmodel.EditMatchViewModel
import org.scoredroid.fragment.compose.composeView
import org.scoredroid.ui.theme.ScoredroidTheme

internal class EditMatchFragment(
    vmFactory: ViewModelProvider.Factory,
) : Fragment() {

    private val viewModel by viewModels<EditMatchViewModel> { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return composeView {
            ScoredroidTheme {
                EditMatchScreen(viewModel) {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
}
