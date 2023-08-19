package org.scoredroid.viewmodel.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import org.scoredroid.viewmodel.SavedStateHandleViewModelProvider
import org.scoredroid.viewmodel.ScoredroidViewModelFactory
import javax.inject.Provider

@Module
object ViewModelFactoryModule {

    @Provides
    fun provideViewModelFactory(
        vmProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>,
        vmSavedStateFactories: @JvmSuppressWildcards Map<Class<out ViewModel>, SavedStateHandleViewModelProvider>,
    ): ViewModelProvider.Factory {
        val vmFactories: Map<Class<out ViewModel>, (SavedStateHandle) -> ViewModel> = vmProviders
            .mapValues { entry ->
                {
                    entry.value.get()
                }
            }

        return ScoredroidViewModelFactory(vmFactories + vmSavedStateFactories)
    }
}
