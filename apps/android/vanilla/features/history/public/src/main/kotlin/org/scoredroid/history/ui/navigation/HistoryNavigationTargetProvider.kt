package org.scoredroid.history.ui.navigation

import androidx.fragment.app.Fragment

interface HistoryNavigationTargetProvider {
    fun getNavigationTarget(): Class<out Fragment>
}
