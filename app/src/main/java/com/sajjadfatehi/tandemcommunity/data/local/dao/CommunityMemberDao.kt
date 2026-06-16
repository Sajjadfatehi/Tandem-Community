package com.sajjadfatehi.tandemcommunity.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sajjadfatehi.tandemcommunity.data.local.entity.LikedCommunityMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityMemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likeMember(likedMember: LikedCommunityMemberEntity)

    @Query("DELETE FROM liked_community_members WHERE memberId = :memberId")
    suspend fun unlikeMember(memberId: Int)

    @Query("SELECT memberId FROM liked_community_members")
    fun observeLikedMemberIds(): Flow<List<Int>>
}
