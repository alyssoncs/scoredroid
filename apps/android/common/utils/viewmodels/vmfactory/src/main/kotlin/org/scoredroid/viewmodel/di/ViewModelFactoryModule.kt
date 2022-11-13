package org.scoredroid.viewmodel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import org.scoredroid.viewmodel.ScoredroidViewModelFactory
import javax.inject.Provider

@Module
object ViewModelFactoryModule {

    @Provides
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        val factories = providers.mapValues { entry ->
            val provider = entry.value
            provider::get
        }

        return ScoredroidViewModelFactory(factories)
    }
}
