package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.util.extensions.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ElectionsViewModel @Inject constructor(
    private val electionRepository: ElectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectionsUiState.Empty)
    val uiState: StateFlow<ElectionsUiState> = _uiState.asStateFlow()

    init {
        // loadUpcomingElections()
    }

    private fun loadUpcomingElections() {
        viewModelScope.launch {
            Log.d(TAG, "loadUpcomingElections: Loading")
            try {
                val upcomingElections = electionRepository.getUpcomingElections()
                Log.d(TAG, "loadUpcomingElections: Result: ${upcomingElections.size}")
                updateUpcomingElectionsUiState(upcomingElections)
            } catch (e: Exception) {
                //TODO:
                Log.e(TAG, "loadUpcomingElections: Error: ${e}")
                e.printStackTrace()
            }

        }
    }

    private fun updateUpcomingElectionsUiState(upcomingElections: List<Election>) {
        _uiState.update { currentUiState ->
            currentUiState.copy(upcomingElections = upcomingElections)
        }
    }

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}