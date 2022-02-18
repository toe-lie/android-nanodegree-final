package com.example.android.politicalpreparedness.data.source.local

import com.example.android.politicalpreparedness.data.source.local.db.daos.ElectionDao
import com.example.android.politicalpreparedness.di.IoDispatcher
import com.example.android.politicalpreparedness.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ElectionLocalDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val electionDao: ElectionDao
) {
    fun observeIsFollowing(electionId: Int): Flow<Boolean> {
        return electionDao.observeById(electionId).map {
            it != null
        }.flowOn(ioDispatcher)
    }

    suspend fun toggleFollowing(election: Election) {
        withContext(ioDispatcher) {
            val isFollowing = electionDao.getById(election.id) != null
            if (isFollowing) {
                electionDao.deleteById(election.id)
            } else {
                electionDao.insert(election)
            }
        }
    }
}