package org.scoredroid.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import org.scoredroid.di.modules.FeaturesModule
import org.scoredroid.di.modules.UseCasesModule
import org.scoredroid.fragment.di.FragmentFactoryModule
import org.scoredroid.infra.dataaccess.di.InfraImplModule
import org.scoredroid.viewmodel.di.ViewModelFactoryModule

@Component(
    modules = [
        InfraImplModule::class,
        FragmentFactoryModule::class,
        ViewModelFactoryModule::class,
        UseCasesModule::class,
        FeaturesModule::class,
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