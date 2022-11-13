package org.scoredroid.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class ScoredroidFragmentFactory(
    private val factories: Map<Class<out Fragment>, () ->Fragment>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)
        val factory = factories[fragmentClass]

        return factory?.invoke() ?: super.instantiate(classLoader, className)
    }
}
