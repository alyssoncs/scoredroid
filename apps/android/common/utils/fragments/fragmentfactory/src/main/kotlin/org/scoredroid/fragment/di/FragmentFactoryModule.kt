package org.scoredroid.fragment.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import org.scoredroid.fragment.ScoredroidFragmentFactory
import javax.inject.Provider

@Module
object FragmentFactoryModule {

    @Provides
    fun provideFragmentFactory(
        providers: @JvmSuppressWildcards Map<Class<out Fragment>, Provider<Fragment>>
    ): FragmentFactory {
        val factories = providers.mapValues { entry ->
            val provider = entry.value
            provider::get
        }

        return ScoredroidFragmentFactory(factories)
    }
}
