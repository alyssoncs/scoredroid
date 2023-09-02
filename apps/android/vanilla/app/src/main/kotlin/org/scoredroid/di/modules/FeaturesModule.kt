package org.scoredroid.di.modules

import dagger.Module
import org.scoredroid.creatematch.di.CreateMatchFeatureModule
import org.scoredroid.editmatch.di.EditMatchFeatureModule
import org.scoredroid.history.di.HistoryFeatureModule
import org.scoredroid.play.di.PlayFeatureModule

@Module(
    includes = [
        CreateMatchFeatureModule::class,
        EditMatchFeatureModule::class,
        HistoryFeatureModule::class,
        PlayFeatureModule::class,
    ],
)
interface FeaturesModule
