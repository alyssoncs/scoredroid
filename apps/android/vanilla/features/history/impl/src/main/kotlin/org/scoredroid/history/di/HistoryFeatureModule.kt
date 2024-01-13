package org.scoredroid.history.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.scoredroid.creatematch.ui.navigation.CreateMatchNavigationTargetProvider
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProvider
import org.scoredroid.fragment.annotation.FragmentKey
import org.scoredroid.history.ui.controller.MatchHistoryFragment
import org.scoredroid.history.ui.navigation.HistoryNavigationTargetProvider
import org.scoredroid.history.ui.navigation.HistoryNavigationTargetProviderImpl
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel
import org.scoredroid.play.ui.navigation.PlayNavigationTargetProvider
import org.scoredroid.usecase.GetMatchesFlowUseCase
import org.scoredroid.usecase.RemoveMatchUseCase
import org.scoredroid.viewmodel.annotation.ViewModelKey

@Module(
    includes = [
        HistoryFeatureModule.ViewModelModule::class,
        HistoryFeatureModule.FragmentModule::class,
        HistoryFeatureModule.NavigationModule::class,
    ],
)
object HistoryFeatureModule {

    @Module
    object ViewModelModule {
        @[Provides IntoMap ViewModelKey(MatchHistoryViewModel::class)]
        fun provideMatchHistoryViewModel(
            getMatchesFlow: GetMatchesFlowUseCase,
            removeMatch: RemoveMatchUseCase,
        ): ViewModel {
            return MatchHistoryViewModel(getMatchesFlow, removeMatch)
        }
    }

    @Module
    object FragmentModule {
        @[Provides IntoMap FragmentKey(MatchHistoryFragment::class)]
        fun provideMatchHistoryFragment(
            vmFactory: ViewModelProvider.Factory,
            editMatchNavigationTargetProvider: EditMatchNavigationTargetProvider,
            createMatchNavigationTargetProvider: CreateMatchNavigationTargetProvider,
            playNavigationTargetProvider: PlayNavigationTargetProvider,
        ): Fragment {
            return MatchHistoryFragment(
                vmFactory,
                editMatchNavigationTargetProvider,
                createMatchNavigationTargetProvider,
                playNavigationTargetProvider,
            )
        }
    }

    @Module
    object NavigationModule {
        @Provides
        fun provideHistoryNavigationTargetProvider(): HistoryNavigationTargetProvider {
            return HistoryNavigationTargetProviderImpl
        }
    }
}
