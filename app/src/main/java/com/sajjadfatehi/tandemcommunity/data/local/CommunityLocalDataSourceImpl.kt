package com.sajjadfatehi.tandemcommunity.data.local

import androidx.paging.PagingSource
import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityMemberDao
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityMemberEntity
import com.sajjadfatehi.tandemcommunity.data.local.entity.LikedCommunityMemberEntity
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityLocalDataSourceImpl @Inject constructor(
    private val communityMemberDao: CommunityMemberDao
) : CommunityLocalDataSource {

    override fun pagingSource(): PagingSource<Int, CommunityMemberEntity> =
        communityMemberDao.pagingSource()

    override suspend fun upsertMembers(members: List<CommunityMemberEntity>) =
        communityMemberDao.upsertMembers(members)

    override suspend fun clearMembers() =
        communityMemberDao.clearMembers()

    override suspend fun setLiked(memberId: Int, isLiked: Boolean) {
        if (isLiked) {
            communityMemberDao.likeMember(LikedCommunityMemberEntity(memberId))
        } else {
            communityMemberDao.unlikeMember(memberId)
        }
    }

    override fun observeIsLiked(memberId: Int): Flow<Boolean> =
        communityMemberDao.observeIsLiked(memberId)

    override fun observeLikedMemberIds(): Flow<List<Int>> =
        communityMemberDao.observeLikedMemberIds()
}
