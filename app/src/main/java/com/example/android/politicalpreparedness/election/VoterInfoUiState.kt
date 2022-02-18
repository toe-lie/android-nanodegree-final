package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.models.UiMessage

data class VoterInfoUiState(
    val isLoading: Boolean = false,
    val loadingError: UiMessage? = null,
    val election: Election? = null,
    val votingLocationFinderUrl: String = "",
    val ballotInfoUrl: String = "",
    val address: Address? = null,
    val isFollowing: Boolean = false
) {
    val showDate: Boolean = election != null
    val showVotingLocation: Boolean = votingLocationFinderUrl.isNotEmpty()
    val showBallotInfo: Boolean = ballotInfoUrl.isNotEmpty()
    val showAddress: Boolean = address != null
    val hasElectionInformation: Boolean = showVotingLocation || showBallotInfo || showAddress

    companion object {
        val Empty = VoterInfoUiState()
    }
}
