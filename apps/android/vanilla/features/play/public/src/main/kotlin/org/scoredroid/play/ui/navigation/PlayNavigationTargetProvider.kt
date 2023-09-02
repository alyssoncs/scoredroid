package org.scoredroid.play.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment

interface PlayNavigationTargetProvider {
    fun getNavigationTarget(matchId: Long): Pair<Class<out Fragment>, Bundle>
}
