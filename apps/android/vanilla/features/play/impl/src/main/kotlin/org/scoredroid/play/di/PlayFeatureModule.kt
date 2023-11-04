package org.scoredroid.play.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.scoredroid.fragment.annotation.FragmentKey
import org.scoredroid.match.domain.usecase.SaveMatchUseCase
import org.scoredroid.play.ui.controller.PlayFragment
import org.scoredroid.play.ui.navigation.PlayNavigationTargetProvider
import org.scoredroid.play.ui.navigation.PlayNavigationTargetProviderImpl
import org.scoredroid.play.ui.viewmodel.PlayViewModel
import org.scoredroid.usecase.DecrementScoreUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.IncrementScoreUseCase
import org.scoredroid.viewmodel.SavedStateHandleViewModelProvider
import org.scoredroid.viewmodel.annotation.ViewModelKey

@Module(
    includes = [
        PlayFeatureModule.ViewModelModule::class,
        PlayFeatureModule.FragmentModule::class,
        PlayFeatureModule.NavigationModule::class,
    ],
)
object PlayFeatureModule {

    @Module
    object ViewModelModule {
        @[Provides IntoMap ViewModelKey(PlayViewModel::class)]
        fun providePlayViewModel(
            getMatchFlow: GetMatchFlowUseCase,
            incrementScore: IncrementScoreUseCase,
            decrementScore: DecrementScoreUseCase,
            saveMatch: SaveMatchUseCase,
        ): SavedStateHandleViewModelProvider {
            return SavedStateHandleViewModelProvider { savedStateHandle ->
                PlayViewModel(
                    getMatchFlow,
                    incrementScore,
                    decrementScore,
                    saveMatch,
                    savedStateHandle,
                )
            }
        }
    }

    @Module
    object FragmentModule {
        @[Provides IntoMap FragmentKey(PlayFragment::class)]
        fun providePlayFragment(vmFactory: ViewModelProvider.Factory): Fragment {
            return PlayFragment(vmFactory)
        }
    }

    @Module
    object NavigationModule {
        @Provides
        fun providePlayNavigationTargetProvider(): PlayNavigationTargetProvider {
            return PlayNavigationTargetProviderImpl
        }
    }
}
