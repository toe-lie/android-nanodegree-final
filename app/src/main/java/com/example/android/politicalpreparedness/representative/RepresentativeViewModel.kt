package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.models.Representative
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

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields
    val addressLine1 = MutableStateFlow("")
    val addressLine2 = MutableStateFlow("")
    val city = MutableStateFlow("")
    val state = MutableStateFlow("")
    val zip = MutableStateFlow("")

    private val _uiState = MutableStateFlow(RepresentativeUiState.Empty)
    val uiState: StateFlow<RepresentativeUiState> = _uiState.asStateFlow()

    init {
        searchRepresentatives()
    }

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
        Log.d(TAG, "searchRepresentatives: Loading")
        viewModelScope.launch {
            try {
                val representatives = representativeRepository.getRepresentatives("Montana", "us")
                updateUiState(representatives)
            } catch (e: Exception) {
                Log.e(TAG, "searchRepresentatives: Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun updateUiState(representatives: List<Representative>) {
        _uiState.update { currentUiState ->
            currentUiState.copy(representatives = representatives)
        }
    }

    fun search() {

    }

}
