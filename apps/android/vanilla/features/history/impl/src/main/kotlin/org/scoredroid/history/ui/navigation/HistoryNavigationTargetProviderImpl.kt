package org.scoredroid.history.ui.navigation

import androidx.fragment.app.Fragment
import org.scoredroid.history.ui.controller.MatchHistoryFragment

internal object HistoryNavigationTargetProviderImpl : HistoryNavigationTargetProvider {
    override fun getNavigationTarget(): Class<out Fragment> {
        return MatchHistoryFragment::class.java
    }
}
