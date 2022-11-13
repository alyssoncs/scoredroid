package org.scoredroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class ScoredroidViewModelFactory(
    private val factories: Map<Class<out ViewModel>, () -> ViewModel>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val factory = factories[modelClass] ?: error("could not find $modelClass")
        return factory.invoke() as T
    }
}
