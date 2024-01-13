package org.scoredroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.scoredroid.di.ApplicationComponent
import org.scoredroid.fragment.transactions.commitWithReordering

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = ApplicationComponent.instance.fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(savedInstanceState)
    }

    private fun addFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.commitWithReordering {
                val historyNavigationTargetProvider = ApplicationComponent.instance.historyNavigationTargetProvider
                add(R.id.fragment_container, historyNavigationTargetProvider.getNavigationTarget(), null)
            }
        }
    }
}
