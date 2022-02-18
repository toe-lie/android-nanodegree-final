package com.example.android.politicalpreparedness.election

import android.os.Parcelable
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.data.repository.VoterRepository
import com.example.android.politicalpreparedness.models.Division
import com.example.android.politicalpreparedness.models.UiMessage
import com.example.android.politicalpreparedness.models.VoterInfoResponse
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
    private val voterRepository: VoterRepository,
    private val electionRepository: ElectionRepository
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
                observeIsFollowing(it.electionId)
            }
        }
    }

    private fun observeIsFollowing(electionId: Int) {
        viewModelScope.launch {
            electionRepository.observeIsFollowing(electionId).collectLatest {
                updateIsFollowingUiState(it)
            }
        }
    }

    private fun loadVoterInfo(query: Query) {
        viewModelScope.launch {
            updatingLoadingUiState(true)
            updateLoadingErrorUiState(null)
            try {
                val voterInfo = voterRepository.getVoterInfo(query.division, query.electionId)
                updateVoterInfoUiState(voterInfo)
                updatingLoadingUiState(false)
            } catch (e: Exception) {
                updatingLoadingUiState(false)
                updateLoadingErrorUiState(e)
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

    private fun updateVoterInfoUiState(voterInfo: VoterInfoResponse) {
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

    private fun updateIsFollowingUiState(isFollowing: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(isFollowing = isFollowing)
        }
    }

    private fun updatingLoadingUiState(loading: Boolean) {
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

    fun toggleFollowing() {
        viewModelScope.launch {
            _uiState.value.election?.let {
                electionRepository.toggleFollowing(it)
            }
        }
    }

    fun retry() {
        viewModelScope.launch {
            _query.firstOrNull()?.let {
                loadVoterInfo(it)
            }
        }
    }

    @Parcelize
    data class Query(val electionId: Int, val division: Division) : Parcelable
}

private const val SAVED_STATE_KEY_QUERY = "keys.Query"