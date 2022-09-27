package de.lucas.musicsearch.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor() : ViewModel() {
    var chartState by mutableStateOf(true)
}