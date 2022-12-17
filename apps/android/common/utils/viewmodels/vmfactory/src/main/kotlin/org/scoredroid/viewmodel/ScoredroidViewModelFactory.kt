package org.scoredroid.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

class ScoredroidViewModelFactory(
    private val factories: Map<Class<out ViewModel>, (SavedStateHandle) -> ViewModel>,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val factory = factories[modelClass] ?: error("could not find $modelClass")
        return factory.invoke(extras.createSavedStateHandle()) as T
    }
}
