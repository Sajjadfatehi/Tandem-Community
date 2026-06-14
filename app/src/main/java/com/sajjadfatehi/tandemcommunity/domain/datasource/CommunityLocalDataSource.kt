package com.sajjadfatehi.tandemcommunity.domain.datasource

import androidx.paging.PagingSource
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityMemberEntity
import kotlinx.coroutines.flow.Flow

interface CommunityLocalDataSource {
    fun pagingSource(): PagingSource<Int, CommunityMemberEntity>
    suspend fun upsertMembers(members:List<CommunityMemberEntity>)
    suspend fun clearMembers()
    suspend fun setLiked(memberId: Int, isLiked: Boolean)
    fun observeIsLiked(memberId: Int): Flow<Boolean>
    fun observeLikedMemberIds():Flow<List<Int>>
}
