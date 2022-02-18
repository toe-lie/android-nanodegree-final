package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.models.UiMessage
import com.example.android.politicalpreparedness.util.extensions.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionsViewModel @Inject constructor(
    private val electionRepository: ElectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectionsUiState.Empty)
    val uiState: StateFlow<ElectionsUiState> = _uiState.asStateFlow()

    init {
        loadUpcomingElections()
        loadSavedElections()
    }

    private fun loadUpcomingElections() {
        viewModelScope.launch {
            updateUpcomingElectionsLoadingErrorUiState(null)
            updateUpcomingElectionsLoadingUiState(true)
            try {
                val upcomingElections = electionRepository.getUpcomingElections()
                updateUpcomingElectionsUiState(upcomingElections)
                updateUpcomingElectionsLoadingUiState(false)
            } catch (e: Exception) {
                updateUpcomingElectionsLoadingErrorUiState(e)
                updateUpcomingElectionsLoadingUiState(false)
            }
        }
    }

    private fun updateUpcomingElectionsLoadingErrorUiState(e: Exception?) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                upcomingElectionsLoadingError = if (e == null) null else UiMessage(e)
            )
        }
    }

    private fun loadSavedElections() {
        viewModelScope.launch {
            electionRepository.observeSavedElections().collect {
                updateSavedElectionsUiState(it)
            }
        }
    }

    private fun updateUpcomingElectionsLoadingUiState(loading: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(loading)
        }
    }

    private fun updateUpcomingElectionsUiState(upcomingElections: List<Election>) {
        _uiState.update { currentUiState ->
            currentUiState.copy(upcomingElections = upcomingElections)
        }
    }

    private fun updateSavedElectionsUiState(savedElections: List<Election>) {
        _uiState.update { currentUiState ->
            currentUiState.copy(savedElections = savedElections)
        }
    }

    fun retryUpcomingElections() {
        loadUpcomingElections()
    }
}