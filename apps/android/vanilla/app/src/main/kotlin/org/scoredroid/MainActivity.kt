package org.scoredroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import org.scoredroid.di.ApplicationComponent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = ApplicationComponent.instance.fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(savedInstanceState)
    }

    private fun addFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<InitFragment>(R.id.fragment_container)
            }
        }
    }
}
