package com.sajjadfatehi.tandemcommunity.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sajjadfatehi.tandemcommunity.data.paging.CommunityMemberPagingSource
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityLocalDataSource
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember
import com.sajjadfatehi.tandemcommunity.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val localDataSource: CommunityLocalDataSource,
    private val remoteDataSource: CommunityRemoteDataSource
) : CommunityRepository {

    override fun getCommunityPagingData(): Flow<PagingData<CommunityMember>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = 3,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CommunityMemberPagingSource(remoteDataSource) }
    ).flow

    override fun observeLikedMemberIds(): Flow<List<Int>> =
        localDataSource.observeLikedMemberIds()


    override suspend fun toggleLike(memberId: Int, currentState: Boolean) =
        localDataSource.setLiked(memberId = memberId, isLiked = !currentState)

    private companion object {
        const val PAGE_SIZE = 20
    }
}
