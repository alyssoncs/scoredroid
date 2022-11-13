package org.scoredroid.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import org.scoredroid.di.modules.FragmentsModule
import org.scoredroid.di.modules.UseCasesModule
import org.scoredroid.infra.dataaccess.di.InfraImplModule

@Component(
    modules = [
        InfraImplModule::class,
        FragmentsModule::class,
        UseCasesModule::class,
    ]
)
interface ApplicationComponent {

    companion object {
        lateinit var instance: ApplicationComponent
            internal set
    }

    val fragmentFactory: FragmentFactory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}