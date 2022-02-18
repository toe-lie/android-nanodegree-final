package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.models.UiMessage

data class ElectionsUiState(
    val isLoadingUpcomingElections: Boolean = false,
    val upcomingElectionsLoadingError: UiMessage? = null,
    val upcomingElections: List<Election> = emptyList(),
    val savedElections: List<Election> = emptyList()
) {

    val isUpcomingElectionsEmpty: Boolean = !isLoadingUpcomingElections
            && upcomingElections.isEmpty()
            && upcomingElectionsLoadingError == null

    val isSavedElectionsEmpty: Boolean = savedElections.isEmpty()

    companion object {
        val Empty = ElectionsUiState()
    }
}
