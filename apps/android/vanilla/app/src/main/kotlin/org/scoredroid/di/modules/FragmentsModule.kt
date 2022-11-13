package org.scoredroid.di.modules

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.scoredroid.InitFragment
import org.scoredroid.fragment.annotation.FragmentKey
import org.scoredroid.fragment.di.FragmentFactoryModule

@Module(
    includes = [
        FragmentFactoryModule::class,
    ]
)
interface FragmentsModule {

    @[Binds IntoMap FragmentKey(InitFragment::class)]
    fun bindInitFragment(fragment: InitFragment): Fragment

    companion object {

        @Provides
        fun provideInitFragment(): InitFragment {
            return InitFragment()
        }
    }
}