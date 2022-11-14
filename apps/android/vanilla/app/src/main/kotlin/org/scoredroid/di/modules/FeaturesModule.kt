package org.scoredroid.di.modules

import dagger.Module
import org.scoredroid.history.di.HistoryFeatureModule

@Module(
    includes = [
        HistoryFeatureModule::class,
    ]
)
interface FeaturesModule
