package com.sajjadfatehi.tandemcommunity.di

import android.content.Context
import androidx.room.Room
import com.sajjadfatehi.tandemcommunity.data.local.CommunityDatabase
import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityMemberDao
import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityRemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn((SingletonComponent::class))
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideCommunityDatabase(
        @ApplicationContext context: Context
    ): CommunityDatabase {
        return Room.databaseBuilder(
            context,
            CommunityDatabase::class.java,
            "community.db"//TODO:move to constants
        ).build()
    }

    @Provides
    @Singleton
    fun provideCommunityMemberDao(database: CommunityDatabase): CommunityMemberDao =
        database.communityMemberDao()

    @Singleton
    @Provides
    fun provideCommunityRemoteKeyDao(database: CommunityDatabase): CommunityRemoteKeyDao =
        database.communityRemoteKeyDao()

}