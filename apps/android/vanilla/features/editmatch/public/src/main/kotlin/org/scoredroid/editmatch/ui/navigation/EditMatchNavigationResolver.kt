package org.scoredroid.editmatch.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment

interface EditMatchNavigationResolver {
    fun createEditMatchFragment(matchId: Long): Pair<Class<out Fragment>, Bundle>
}
