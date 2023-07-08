package org.scoredroid.editmatch.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment

interface EditMatchNavigationTargetProvider {
    fun getNavigationTarget(matchId: Long): Pair<Class<out Fragment>, Bundle>
}
