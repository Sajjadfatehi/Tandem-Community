package com.sajjadfatehi.tandemcommunity.domain.datasource

import kotlinx.coroutines.flow.Flow

interface CommunityLocalDataSource {

    suspend fun setLiked(memberId: Int, isLiked: Boolean)
    fun observeLikedMemberIds(): Flow<List<Int>>
}
