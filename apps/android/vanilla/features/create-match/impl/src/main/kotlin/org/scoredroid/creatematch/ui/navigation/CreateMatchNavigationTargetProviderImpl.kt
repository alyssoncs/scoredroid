package org.scoredroid.creatematch.ui.navigation

import androidx.fragment.app.Fragment
import org.scoredroid.creatematch.ui.controller.CreateMatchFragment

object CreateMatchNavigationTargetProviderImpl : CreateMatchNavigationTargetProvider {
    override fun getNavigationTarget(): Class<out Fragment> {
        return CreateMatchFragment::class.java
    }
}
