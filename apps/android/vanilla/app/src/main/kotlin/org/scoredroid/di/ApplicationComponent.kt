package org.scoredroid.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.scoredroid.di.modules.UseCasesModule
import org.scoredroid.infra.dataaccess.di.InfraImplModule
import org.scoredroid.score.domain.usecase.IncrementScoreUseCase

@Component(
    modules = [
        InfraImplModule::class,
        UseCasesModule::class,
    ]
)
interface ApplicationComponent {

    companion object {
        lateinit var instance: ApplicationComponent
            internal set
    }

    fun increment(): IncrementScoreUseCase

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}