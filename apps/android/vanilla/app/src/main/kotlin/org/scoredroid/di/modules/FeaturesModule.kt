package org.scoredroid.di.modules

import dagger.Module
import org.scoredroid.editmatch.di.EditMatchFeatureModule
import org.scoredroid.history.di.HistoryFeatureModule

@Module(
    includes = [
        HistoryFeatureModule::class,
        EditMatchFeatureModule::class,
    ]
)
interface FeaturesModule
