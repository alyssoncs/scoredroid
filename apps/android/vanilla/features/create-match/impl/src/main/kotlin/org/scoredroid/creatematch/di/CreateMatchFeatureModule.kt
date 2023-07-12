package org.scoredroid.creatematch.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.scoredroid.creatematch.ui.controller.CreateMatchFragment
import org.scoredroid.creatematch.ui.navigation.CreateMatchNavigationTargetProvider
import org.scoredroid.creatematch.ui.navigation.CreateMatchNavigationTargetProviderImpl
import org.scoredroid.creatematch.ui.viewmodel.CreateMatchViewModel
import org.scoredroid.fragment.annotation.FragmentKey
import org.scoredroid.match.domain.usecase.CreateMatchUseCase
import org.scoredroid.viewmodel.annotation.ViewModelKey

@Module(
    includes = [
        CreateMatchFeatureModule.ViewModelModule::class,
        CreateMatchFeatureModule.FragmentModule::class,
        CreateMatchFeatureModule.NavigationModule::class,
    ],
)
object CreateMatchFeatureModule {

    @Module
    object ViewModelModule {
        @[Provides IntoMap ViewModelKey(CreateMatchViewModel::class)]
        fun provideCreateMatchViewModel(
            createMatch: CreateMatchUseCase,
        ): ViewModel {
            return CreateMatchViewModel(createMatch = createMatch)
        }
    }

    @Module
    object FragmentModule {
        @[Provides IntoMap FragmentKey(CreateMatchFragment::class)]
        fun provideCreateMatchFragment(vmFactory: ViewModelProvider.Factory): Fragment {
            return CreateMatchFragment(vmFactory)
        }
    }

    @Module
    object NavigationModule {
        @Provides
        fun provideCreateMatchNavigationTargetProvider(): CreateMatchNavigationTargetProvider {
            return CreateMatchNavigationTargetProviderImpl
        }
    }
}
