package com.sajjadfatehi.tandemcommunity.data.local

import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityMemberDao
import com.sajjadfatehi.tandemcommunity.data.local.entity.LikedCommunityMemberEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityLocalDataSourceImplTest {

    private val dao = mockk<CommunityMemberDao>()

    private lateinit var dataSource: CommunityLocalDataSourceImpl

    @Before
    fun setup() {
        dataSource = CommunityLocalDataSourceImpl(dao)
    }

    @Test
    fun `setLiked should call likeMember when isLiked is true`() = runTest {
        coEvery { dao.likeMember(any()) } just Runs

        dataSource.setLiked(
            memberId = 123,
            isLiked = true
        )

        coVerify(exactly = 1) {
            dao.likeMember(LikedCommunityMemberEntity(123))
        }

        coVerify(exactly = 0) {
            dao.unlikeMember(any())
        }
    }

    @Test
    fun `setLiked should call unlikeMember when isLiked is false`() = runTest {
        coEvery { dao.unlikeMember(any()) } just Runs

        dataSource.setLiked(
            memberId = 123,
            isLiked = false
        )

        coVerify(exactly = 1) {
            dao.unlikeMember(123)
        }

        coVerify(exactly = 0) {
            dao.likeMember(any())
        }
    }

    @Test
    fun `observeLikedMemberIds should return dao flow`() = runTest {
        val expected = flowOf(listOf(1, 2, 3))

        every { dao.observeLikedMemberIds() } returns expected

        val result = dataSource.observeLikedMemberIds()

        assertSame(expected, result)
    }
}