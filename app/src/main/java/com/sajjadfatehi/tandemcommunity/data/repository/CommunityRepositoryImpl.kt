package com.sajjadfatehi.tandemcommunity.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sajjadfatehi.tandemcommunity.data.local.CommunityDatabase
import com.sajjadfatehi.tandemcommunity.data.mapper.toCommunityMember
import com.sajjadfatehi.tandemcommunity.data.paging.CommunityRemoteMediator
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityLocalDataSource
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember
import com.sajjadfatehi.tandemcommunity.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CommunityRepositoryImpl @Inject constructor(
    private val database: CommunityDatabase,
    private val localDataSource: CommunityLocalDataSource,
    private val remoteDataSource: CommunityRemoteDataSource
) : CommunityRepository {

    override fun getCommunityStream(): Flow<PagingData<CommunityMember>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CommunityRemoteMediator(
                database = database,
                remoteDataSource = remoteDataSource
            ),
            pagingSourceFactory = { localDataSource.pagingSource() }
        ).flow.combine(localDataSource.observeLikedMemberIds()) { pagingData, likedMemberIds ->
            val likedMemberIdSet = likedMemberIds.toSet()
            pagingData.map { member ->
                member.toCommunityMember(isLiked = member.id in likedMemberIdSet)
            }
        }
    }

    override fun getLikeState(memberId: Int): Flow<Boolean> =
        localDataSource.observeIsLiked(memberId)

    override suspend fun toggleLike(memberId: Int, currentState: Boolean) =
        localDataSource.setLiked(memberId = memberId, isLiked = !currentState)

    private companion object {
        const val PAGE_SIZE = 20
    }
}
