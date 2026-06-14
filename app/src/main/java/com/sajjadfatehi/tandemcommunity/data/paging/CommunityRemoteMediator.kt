package com.sajjadfatehi.tandemcommunity.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sajjadfatehi.tandemcommunity.data.local.CommunityDatabase
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityMemberEntity
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityRemoteKeyEntity
import com.sajjadfatehi.tandemcommunity.data.mapper.toEntity
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CommunityRemoteMediator @Inject constructor(
    private val database: CommunityDatabase,
    private val remoteDataSource: CommunityRemoteDataSource
) : RemoteMediator<Int, CommunityMemberEntity>() {

    private val memberDao = database.communityMemberDao()
    private val remoteKeyDao = database.communityRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CommunityMemberEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val members = remoteDataSource.getCommunityMembers(page)
            val endOfPaginationReached = members.size < PAGE_SIZE

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    memberDao.clearMembers()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = members.map { member ->
                    CommunityRemoteKeyEntity(
                        memberId = member.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeyDao.upsertRemoteKeys(keys)
                memberDao.upsertMembers(
                    members.mapIndexed { index, member ->
                        member.toEntity(page = page, indexInPage = index)
                    }
                )
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CommunityMemberEntity>
    ): CommunityRemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { memberId ->
                remoteKeyDao.remoteKeyByMemberId(memberId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CommunityMemberEntity>
    ): CommunityRemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data
            ?.firstOrNull()
            ?.let { member -> remoteKeyDao.remoteKeyByMemberId(member.id) }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CommunityMemberEntity>
    ): CommunityRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data
            ?.lastOrNull()
            ?.let { member -> remoteKeyDao.remoteKeyByMemberId(member.id) }
    }

    private companion object {
        const val STARTING_PAGE_INDEX = 1
        const val PAGE_SIZE = 20
    }
}
