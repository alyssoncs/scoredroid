package org.scoredroid.editmatch.ui.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.scoredroid.editmatch.ui.controller.EditMatchFragment

object EditMatchNavigationResolverImpl : EditMatchNavigationResolver {
    override fun createEditMatchFragment(matchId: Long): Pair<Class<out Fragment>, Bundle> {
        return Pair(EditMatchFragment::class.java, bundleOf(MATCH_ID_NAV_ARG to matchId))
    }
}
