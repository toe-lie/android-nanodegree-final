package com.example.android.politicalpreparedness.representative

import com.example.android.politicalpreparedness.models.Representative

data class RepresentativeUiState(
    val representatives: List<Representative> = emptyList()
) {
    companion object {
        val Empty = RepresentativeUiState()
    }
}
