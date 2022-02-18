package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.models.Representative
import com.example.android.politicalpreparedness.models.UiMessage
import com.example.android.politicalpreparedness.util.extensions.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SAVED_STATE_KEY_REQUESTING = "key.REQUESTING"

@HiltViewModel
class RepresentativeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val representativeRepository: RepresentativeRepository
) : ViewModel() {

    val addressLine1 = MutableStateFlow("")
    val addressLine2 = MutableStateFlow("")
    val city = MutableStateFlow("")
    val state = MutableStateFlow("")
    val zip = MutableStateFlow("")

    private val _uiState = MutableStateFlow(RepresentativeUiState.Empty)
    val uiState: StateFlow<RepresentativeUiState> = _uiState.asStateFlow()

    fun setRequestingLocationUpdates(requesting: Boolean) {
        savedStateHandle[SAVED_STATE_KEY_REQUESTING] = requesting
    }

    fun getRequestingLocationUpdates(): Boolean {
        return savedStateHandle.get<Boolean>(SAVED_STATE_KEY_REQUESTING) ?: false
    }

    fun setAddress(address: Address) {
        addressLine1.value = address.line1
        addressLine2.value = address.line2.orEmpty()
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
    }

    private fun searchRepresentatives() {
        if (state.value.isEmpty() || city.value.isEmpty()) {
            return
        }

        viewModelScope.launch {
            markAsSearched()
            updateLoadingUiState(true)
            updateLoadingErrorUiState(null)
            try {
                val representatives =
                    representativeRepository.getRepresentatives(state.value, city.value)
                updateUiState(representatives)
                updateLoadingUiState(false)
            } catch (e: Exception) {
                updateLoadingUiState(false)
                updateLoadingErrorUiState(e)
            }
        }
    }

    private fun markAsSearched() {
        _uiState.update {
            it.copy(searched = true)
        }
    }

    private fun updateUiState(representatives: List<Representative>) {
        _uiState.update { currentUiState ->
            currentUiState.copy(representatives = representatives)
        }
    }

    fun search() {
        val isAllInputValid = validateInputs()
        if (isAllInputValid) {
            searchRepresentatives()
        }
    }

    private fun validateInputs(): Boolean {
        var isAllInputValid = true

        if (city.value.isEmpty()) {
            _uiState.update {
                it.copy(cityValidationError = UiMessage.ResourceMessage(message = R.string.error_input_required))
            }
            isAllInputValid = false
        } else {
            _uiState.update {
                it.copy(cityValidationError = null)
            }
        }

        if (state.value.isEmpty()) {
            _uiState.update {
                it.copy(stateValidationError = UiMessage.ResourceMessage(message = R.string.error_input_required))
            }
            isAllInputValid = false
        } else {
            _uiState.update {
                it.copy(stateValidationError = null)
            }
        }

        return isAllInputValid
    }

    fun retry() {
        searchRepresentatives()
    }

    private fun updateLoadingUiState(loading: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(isLoading = loading)
        }
    }

    private fun updateLoadingErrorUiState(e: Exception?) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                loadingError = if (e == null) null else UiMessage(e)
            )
        }
    }

}
