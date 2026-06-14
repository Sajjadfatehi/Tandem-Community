package com.sajjadfatehi.tandemcommunity.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityRemoteKeyEntity

@Dao
interface CommunityRemoteKeyDao {

    @Query("SELECT * FROM community_remote_keys WHERE memberId = :memberId")
    suspend fun remoteKeyByMemberId(memberId: Int): CommunityRemoteKeyEntity?

    @Upsert
    suspend fun upsertRemoteKeys(keys: List<CommunityRemoteKeyEntity>)

    @Query("DELETE FROM community_remote_keys")
    suspend fun clearRemoteKeys()
}
