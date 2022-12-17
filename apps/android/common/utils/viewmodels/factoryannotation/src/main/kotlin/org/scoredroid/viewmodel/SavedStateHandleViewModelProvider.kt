package org.scoredroid.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

fun interface SavedStateHandleViewModelProvider : (SavedStateHandle) -> ViewModel
