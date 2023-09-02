package org.scoredroid.play.ui.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.scoredroid.play.ui.controller.PlayFragment

object PlayNavigationTargetProviderImpl : PlayNavigationTargetProvider {
    override fun getNavigationTarget(matchId: Long): Pair<Class<out Fragment>, Bundle> {
        return PlayFragment::class.java to bundleOf(MATCH_ID_NAV_ARG to matchId)
    }
}
