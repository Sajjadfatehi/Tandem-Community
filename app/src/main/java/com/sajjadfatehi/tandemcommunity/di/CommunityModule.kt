package com.sajjadfatehi.tandemcommunity.di

import com.sajjadfatehi.tandemcommunity.data.local.CommunityLocalDataSourceImpl
import com.sajjadfatehi.tandemcommunity.data.remote.CommunityRemoteDataSourceImpl
import com.sajjadfatehi.tandemcommunity.data.repository.CommunityRepositoryImpl
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityLocalDataSource
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import com.sajjadfatehi.tandemcommunity.domain.repository.CommunityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommunityModule {

    @Binds
    @Singleton
    abstract fun bindCommunityLocalDataSource(local: CommunityLocalDataSourceImpl): CommunityLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCommunityRemoteDataSource(remote: CommunityRemoteDataSourceImpl): CommunityRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCommunityRepository(repository: CommunityRepositoryImpl): CommunityRepository

}
