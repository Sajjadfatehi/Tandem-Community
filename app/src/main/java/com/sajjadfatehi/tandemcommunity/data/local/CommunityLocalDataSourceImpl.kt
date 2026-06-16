package com.sajjadfatehi.tandemcommunity.data.local

import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityMemberDao
import com.sajjadfatehi.tandemcommunity.data.local.entity.LikedCommunityMemberEntity
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityLocalDataSourceImpl @Inject constructor(
    private val communityMemberDao: CommunityMemberDao
) : CommunityLocalDataSource {


    override suspend fun setLiked(memberId: Int, isLiked: Boolean) {
        if (isLiked) {
            communityMemberDao.likeMember(LikedCommunityMemberEntity(memberId))
        } else {
            communityMemberDao.unlikeMember(memberId)
        }
    }

    override fun observeLikedMemberIds(): Flow<List<Int>> =
        communityMemberDao.observeLikedMemberIds()
}
