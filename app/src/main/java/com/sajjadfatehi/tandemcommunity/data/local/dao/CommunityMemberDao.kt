package com.sajjadfatehi.tandemcommunity.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityMemberEntity
import com.sajjadfatehi.tandemcommunity.data.local.entity.LikedCommunityMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityMemberDao {

    @Query("SELECT * FROM community_members ORDER BY page ASC, indexInPage ASC")
    fun pagingSource(): PagingSource<Int, CommunityMemberEntity>

    @Upsert
    suspend fun upsertMembers(members: List<CommunityMemberEntity>)

    @Query("DELETE FROM community_members")
    suspend fun clearMembers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likeMember(likedMember: LikedCommunityMemberEntity)

    @Query("DELETE FROM liked_community_members WHERE memberId = :memberId")
    suspend fun unlikeMember(memberId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM liked_community_members WHERE memberId = :memberId)")
    fun observeIsLiked(memberId: Int): Flow<Boolean>

    @Query("SELECT memberId FROM liked_community_members")
    fun observeLikedMemberIds(): Flow<List<Int>>
}
