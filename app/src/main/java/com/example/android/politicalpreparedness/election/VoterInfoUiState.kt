package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.models.Election

data class VoterInfoUiState(
    val election: Election? = null,
    val votingLocationFinderUrl: String = "",
    val ballotInfoUrl: String = "",
    val address: Address? = null
) {

    val showVotingLocation: Boolean = votingLocationFinderUrl.isNotEmpty()
    val showBallotInfo: Boolean = ballotInfoUrl.isNotEmpty()
    val showAddress: Boolean = address != null

    companion object {
        val Empty = VoterInfoUiState()
    }
}
