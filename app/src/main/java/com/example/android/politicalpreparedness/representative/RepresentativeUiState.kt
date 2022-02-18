package com.example.android.politicalpreparedness.representative

import com.example.android.politicalpreparedness.models.Representative
import com.example.android.politicalpreparedness.models.UiMessage

data class RepresentativeUiState(
    val representatives: List<Representative> = emptyList(),
    val isLoading: Boolean = false,
    val loadingError: UiMessage? = null,
    val cityValidationError: UiMessage? = null,
    val stateValidationError: UiMessage? = null,
    val searched: Boolean = false
) {
    val isListEmpty: Boolean = searched
            && !isLoading
            && representatives.isEmpty()
            && loadingError == null

    companion object {
        val Empty = RepresentativeUiState()
    }
}
