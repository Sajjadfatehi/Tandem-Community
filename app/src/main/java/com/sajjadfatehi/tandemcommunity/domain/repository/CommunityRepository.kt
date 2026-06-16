package com.sajjadfatehi.tandemcommunity.domain.repository

import androidx.paging.PagingData
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun getCommunityPagingData(): Flow<PagingData<CommunityMember>>
    fun observeLikedMemberIds(): Flow<List<Int>>
    suspend fun toggleLike(memberId: Int, currentState: Boolean)
}
