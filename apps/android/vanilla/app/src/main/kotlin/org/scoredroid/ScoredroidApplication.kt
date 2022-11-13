package org.scoredroid

import android.app.Application
import org.scoredroid.di.ApplicationComponent
import org.scoredroid.di.DaggerApplicationComponent

class ScoredroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val component = DaggerApplicationComponent.factory().create(context = this)
        ApplicationComponent.instance = component
    }
}
