package org.scoredroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import org.scoredroid.di.ApplicationComponent
import org.scoredroid.fragment.transactions.commitWithReordering
import org.scoredroid.history.ui.controller.MatchHistoryFragment

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
                add<MatchHistoryFragment>(R.id.fragment_container)
            }
        }
    }
}
