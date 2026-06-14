package com.sajjadfatehi.tandemcommunity.domain.repository

import androidx.paging.PagingData
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun getCommunityStream(): Flow<PagingData<CommunityMember>>
    fun getLikeState(memberId: Int): Flow<Boolean>
    suspend fun toggleLike(memberId: Int, currentState: Boolean)
}
