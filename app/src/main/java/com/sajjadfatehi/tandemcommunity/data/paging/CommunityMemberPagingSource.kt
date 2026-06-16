package com.sajjadfatehi.tandemcommunity.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sajjadfatehi.tandemcommunity.core.Result
import com.sajjadfatehi.tandemcommunity.data.mapper.toCommunityMember
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember

class CommunityMemberPagingSource(private val communityRemoteDataSource: CommunityRemoteDataSource) :
    PagingSource<Int, CommunityMember>() {

    override fun getRefreshKey(state: PagingState<Int, CommunityMember>): Int? {
        return PagingSourceUtils.getRefreshKey(state.anchorPosition, state)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommunityMember> {

        val position = params.key ?: COMMUNITY_PAGE_INDEX
        val result = communityRemoteDataSource.getCommunityMembers(page = position)

        if (result is Result.Success) {
            Log.d(
                "Paging",
                "page=$position returned=${result.data.size}"
            )
        }
        return when (result) {
            is Result.Error -> LoadResult.Error(result.exception)
            is Result.Loading -> LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )

            is Result.Success -> {
                val members = result.data.map { it.toCommunityMember() }
                LoadResult.Page(
                    data = members,
                    prevKey = PagingSourceUtils.calculatePrevKey(position, COMMUNITY_PAGE_INDEX),
                    nextKey = PagingSourceUtils.calculateNextKey(
                        result.data.isEmpty(),
                        position
                    )
                )
            }
        }
    }

}
