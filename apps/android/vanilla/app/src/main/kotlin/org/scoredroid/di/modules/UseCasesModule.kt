package org.scoredroid.di.modules

import dagger.Module

@Module(
    includes = [
        MatchUseCasesModule::class,
        TeamsUseCasesModule::class,
    ],
)
object UseCasesModule
