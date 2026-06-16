package com.sajjadfatehi.tandemcommunity.data.repository

import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityLocalDataSource
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityRepositoryImplTest {

    private val localDataSource = mockk<CommunityLocalDataSource>()
    private val remoteDataSource = mockk<CommunityRemoteDataSource>()

    private lateinit var repository: CommunityRepositoryImpl

    @Before
    fun setup() {
        repository = CommunityRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `observeLikedMemberIds should return local datasource flow`() = runTest {
        val expectedIds = listOf(1, 2, 3)

        every {
            localDataSource.observeLikedMemberIds()
        } returns flowOf(expectedIds)

        val result = repository.observeLikedMemberIds().first()

        assertEquals(expectedIds, result)
    }

    @Test
    fun `toggleLike should set liked true when current state is false`() = runTest {
        coEvery {
            localDataSource.setLiked(any(), any())
        } just Runs

        repository.toggleLike(
            memberId = 42,
            currentState = false
        )

        coVerify(exactly = 1) {
            localDataSource.setLiked(
                memberId = 42,
                isLiked = true
            )
        }
    }

    @Test
    fun `toggleLike should set liked false when current state is true`() = runTest {
        coEvery {
            localDataSource.setLiked(any(), any())
        } just Runs

        repository.toggleLike(
            memberId = 42,
            currentState = true
        )

        coVerify(exactly = 1) {
            localDataSource.setLiked(
                memberId = 42,
                isLiked = false
            )
        }
    }
}