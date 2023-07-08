package org.scoredroid.history.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProvider
import org.scoredroid.fragment.annotation.FragmentKey
import org.scoredroid.history.ui.controller.MatchHistoryFragment
import org.scoredroid.history.ui.viewmodel.MatchHistoryViewModel
import org.scoredroid.match.domain.usecase.GetMatchesUseCase
import org.scoredroid.viewmodel.annotation.ViewModelKey

@Module(
    includes = [
        HistoryFeatureModule.ViewModelModule::class,
        HistoryFeatureModule.FragmentModule::class,
    ],
)
object HistoryFeatureModule {

    @Module
    object ViewModelModule {
        @[Provides IntoMap ViewModelKey(MatchHistoryViewModel::class)]
        fun provideMatchHistoryViewModel(
            getMatches: GetMatchesUseCase,
        ): ViewModel {
            return MatchHistoryViewModel(getMatches)
        }
    }

    @Module
    object FragmentModule {
        @[Provides IntoMap FragmentKey(MatchHistoryFragment::class)]
        fun provideMatchHistoryFragment(
            vmFactory: ViewModelProvider.Factory,
            editMatchNavigationTargetProvider: EditMatchNavigationTargetProvider,
        ): Fragment {
            return MatchHistoryFragment(vmFactory, editMatchNavigationTargetProvider)
        }
    }
}
