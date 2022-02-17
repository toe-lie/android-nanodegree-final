package com.example.android.politicalpreparedness.di

import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.data.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.data.repository.VoterRepository
import com.example.android.politicalpreparedness.data.repository.impl.DefaultElectionRepository
import com.example.android.politicalpreparedness.data.repository.impl.DefaultRepresentativeRepository
import com.example.android.politicalpreparedness.data.repository.impl.DefaultVoterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindElectionRepository(db: DefaultElectionRepository): ElectionRepository

    @Binds
    abstract fun bindVoterRepository(db: DefaultVoterRepository): VoterRepository

    @Binds
    abstract fun bindRepresentativeRepository(db: DefaultRepresentativeRepository): RepresentativeRepository

}