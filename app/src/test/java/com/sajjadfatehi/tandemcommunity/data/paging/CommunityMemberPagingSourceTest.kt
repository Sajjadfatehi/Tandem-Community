package com.sajjadfatehi.tandemcommunity.data.paging

import androidx.paging.PagingSource
import com.sajjadfatehi.tandemcommunity.core.Result
import com.sajjadfatehi.tandemcommunity.data.mapper.toCommunityMember
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityMemberPagingSourceTest {

    private val remoteDataSource = mockk<CommunityRemoteDataSource>()

    private lateinit var pagingSource: CommunityMemberPagingSource

    @Before
    fun setup() {
        pagingSource = CommunityMemberPagingSource(remoteDataSource)
    }

    @Test
    fun `load should return page when api succeeds`() = runTest {
        val response = listOf(
            createMockCommunityMemberDto(id = 1),
            createMockCommunityMemberDto(id = 2)
        )

        coEvery {
            remoteDataSource.getCommunityMembers(COMMUNITY_PAGE_INDEX)
        } returns Result.Success(response)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = response.map { it.toCommunityMember() },
                prevKey = null,
                nextKey = 2
            ),
            result
        )
    }

    @Test
    fun `load should return page with null next key when api returns empty list`() =
        runTest {
            coEvery {
                remoteDataSource.getCommunityMembers(COMMUNITY_PAGE_INDEX)
            } returns Result.Success(emptyList())

            val result = pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 20,
                    placeholdersEnabled = false
                )
            )

            assertEquals(
                PagingSource.LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                ),
                result
            )
        }

    @Test
    fun `load should return error when api fails`() = runTest {
        val exception = IllegalStateException("Network error")

        coEvery {
            remoteDataSource.getCommunityMembers(COMMUNITY_PAGE_INDEX)
        } returns Result.Error(exception)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(
            exception,
            (result as PagingSource.LoadResult.Error).throwable
        )
    }

    @Test
    fun `load should return empty page when loading result returned`() = runTest {
        coEvery {
            remoteDataSource.getCommunityMembers(COMMUNITY_PAGE_INDEX)
        } returns Result.Loading

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            ),
            result
        )
    }

    private fun createMockCommunityMemberDto(
        id: Int = 1
    ): CommunityMemberDto {
        return CommunityMemberDto(
            id = id,
            firstName = "Sajjad",
            learns = listOf("English"),
            natives = listOf("Persian"),
            pictureUrl = "some picture url",
            referenceCnt = 10,
            topic = "Android"
        )
    }
}