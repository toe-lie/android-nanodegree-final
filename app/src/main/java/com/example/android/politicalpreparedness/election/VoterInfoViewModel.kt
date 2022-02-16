package com.example.android.politicalpreparedness.election

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.repository.VoterRepository
import com.example.android.politicalpreparedness.data.source.local.db.daos.ElectionDao
import com.example.android.politicalpreparedness.models.Division
import com.example.android.politicalpreparedness.models.VoterInfoResponse
import com.example.android.politicalpreparedness.util.extensions.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class VoterInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val voterRepository: VoterRepository
) : ViewModel() {

    private val _query: Flow<Query> =
        savedStateHandle.getLiveData<Query?>(SAVED_STATE_KEY_QUERY).asFlow()

    private val _uiState = MutableStateFlow(VoterInfoUiState.Empty)
    val uiState: StateFlow<VoterInfoUiState> = _uiState.asStateFlow()

    init {
        observeQuery()
    }

    private fun observeQuery() {
        viewModelScope.launch {
            _query.collectLatest {
                loadVoterInfo(it)
            }
        }
    }

    private fun loadVoterInfo(query: Query) {
        viewModelScope.launch {
            Log.d(TAG, "loadVoterInfo: Loading: $query")
            try {
                val voterInfo = voterRepository.getVoterInfo(query.division, query.electionId)
                updateUiState(voterInfo)
                Log.d(TAG, "loadVoterInfo: Success: Query: $query; VoterInfo: $voterInfo")
            } catch (e: Exception) {
                Log.e(TAG, "loadVoterInfo: error: $e")
            }
        }
    }

    fun setQuery(electionId: Int, division: Division) {
        val query: Query? = savedStateHandle.get<Query?>(SAVED_STATE_KEY_QUERY)
        val updated = Query(electionId, division)
        if (query != null && query == updated) {
            return
        }

        savedStateHandle[SAVED_STATE_KEY_QUERY] = updated
    }

    private fun updateUiState(voterInfo: VoterInfoResponse) {
        _uiState.update { currentUiState ->
            val electionAdministrationBody =
                voterInfo.state?.getOrNull(0)?.electionAdministrationBody
            if (electionAdministrationBody != null) {
                currentUiState.copy(
                    election = voterInfo.election,
                    votingLocationFinderUrl = electionAdministrationBody.votingLocationFinderUrl.orEmpty(),
                    ballotInfoUrl = electionAdministrationBody.ballotInfoUrl.orEmpty(),
                    address = electionAdministrationBody.correspondenceAddress
                )
            } else {
                currentUiState
            }
        }
    }
    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */


    @Parcelize
    data class Query(val electionId: Int, val division: Division) : Parcelable
}

private val SAVED_STATE_KEY_QUERY = "keys.Query"