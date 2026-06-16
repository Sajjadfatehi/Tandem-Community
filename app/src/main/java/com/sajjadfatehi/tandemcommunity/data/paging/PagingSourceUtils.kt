package com.sajjadfatehi.tandemcommunity.data.paging

import androidx.paging.PagingState

internal const val COMMUNITY_PAGE_INDEX = 1

internal object PagingSourceUtils {
    fun getRefreshKey(anchorPosition: Int?, pagingState: PagingState<Int, *>): Int? {
        return anchorPosition?.let {
            pagingState.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: pagingState.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    fun calculatePrevKey(position: Int, initialPageIndex: Int): Int? {
        return if (position == initialPageIndex) null else position - 1
    }

    fun calculateNextKey(isDataEmpty: Boolean, position: Int): Int? {
        return if (isDataEmpty) null else position + 1
    }
}
