package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.models.Election

data class ElectionsUiState(
    val upcomingElections: List<Election> = emptyList(),
    val savedElections: List<Election> = emptyList()
) {
    companion object {
        val Empty = ElectionsUiState()
    }
}
