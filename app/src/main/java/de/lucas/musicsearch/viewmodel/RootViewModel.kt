package de.lucas.musicsearch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor() : ViewModel() {
    var loadingState by mutableStateOf(LoadingState.DEFAULT)
}

enum class LoadingState {
    DEFAULT,
    LOADING,
    ERROR
}