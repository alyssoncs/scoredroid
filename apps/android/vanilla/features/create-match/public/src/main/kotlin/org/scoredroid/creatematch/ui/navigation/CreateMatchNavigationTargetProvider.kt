package org.scoredroid.creatematch.ui.navigation

import androidx.fragment.app.Fragment

interface CreateMatchNavigationTargetProvider {
    fun getNavigationTarget(): Class<out Fragment>
}
