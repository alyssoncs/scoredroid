package org.scoredroid.editmatch.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.scoredroid.editmatch.ui.controller.EditMatchFragment
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProvider
import org.scoredroid.editmatch.ui.navigation.EditMatchNavigationTargetProviderImpl
import org.scoredroid.editmatch.ui.viewmodel.EditMatchViewModel
import org.scoredroid.fragment.annotation.FragmentKey
import org.scoredroid.usecase.AddTeamUseCase
import org.scoredroid.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.usecase.CreateMatchUseCase
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.RenameMatchUseCase
import org.scoredroid.usecase.RenameTeamUseCase
import org.scoredroid.usecase.SaveMatchUseCase
import org.scoredroid.viewmodel.SavedStateHandleViewModelProvider
import org.scoredroid.viewmodel.annotation.ViewModelKey

@Module(
    includes = [
        EditMatchFeatureModule.ViewModelModule::class,
        EditMatchFeatureModule.FragmentModule::class,
        EditMatchFeatureModule.NavigationModule::class,
    ],
)
object EditMatchFeatureModule {

    @Module
    object ViewModelModule {
        @[Provides IntoMap ViewModelKey(EditMatchViewModel::class)]
        fun provideEditMatchViewModel(
            createMatch: CreateMatchUseCase,
            getMatchFlow: GetMatchFlowUseCase,
            renameMatch: RenameMatchUseCase,
            renameTeam: RenameTeamUseCase,
            addTeam: AddTeamUseCase,
            saveMatch: SaveMatchUseCase,
            clearTransientData: ClearTransientMatchDataUseCase,
        ): SavedStateHandleViewModelProvider {
            return SavedStateHandleViewModelProvider { savedStateHandle: SavedStateHandle ->
                EditMatchViewModel(
                    createMatch = createMatch,
                    getMatchFlow = getMatchFlow,
                    renameMatch = renameMatch,
                    renameTeam = renameTeam,
                    addTeam = addTeam,
                    saveMatch = saveMatch,
                    clearTransientData = clearTransientData,
                    savedStateHandle = savedStateHandle,
                )
            }
        }
    }

    @Module
    object FragmentModule {
        @[Provides IntoMap FragmentKey(EditMatchFragment::class)]
        fun provideEditMatchFragment(vmFactory: ViewModelProvider.Factory): Fragment {
            return EditMatchFragment(vmFactory)
        }
    }

    @Module
    object NavigationModule {
        @Provides
        fun provideEditMatchNavigationTargetProvider(): EditMatchNavigationTargetProvider {
            return EditMatchNavigationTargetProviderImpl
        }
    }
}
