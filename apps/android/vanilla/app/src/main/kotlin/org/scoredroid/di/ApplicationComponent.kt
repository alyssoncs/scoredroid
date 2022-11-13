package org.scoredroid.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.scoredroid.infra.dataaccess.di.InfraImplModule

@Component(
    modules = [
        InfraImplModule::class,
    ]
)
interface ApplicationComponent {

    companion object {
        lateinit var instance: ApplicationComponent
            internal set
    }

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}