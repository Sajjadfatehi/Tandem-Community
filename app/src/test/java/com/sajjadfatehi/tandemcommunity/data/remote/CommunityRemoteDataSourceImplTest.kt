package com.sajjadfatehi.tandemcommunity.data.remote

import com.sajjadfatehi.tandemcommunity.core.Result
import com.sajjadfatehi.tandemcommunity.data.remote.api.CommunityApiService
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityRemoteDataSourceImplTest {

    private val apiService = mockk<CommunityApiService>()

    private lateinit var dataSource: CommunityRemoteDataSourceImpl

    @Before
    fun setup() {
        dataSource = CommunityRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getCommunityMembers returns members when request succeeds`() = runTest {
        val members = listOf(
            CommunityMemberDto(
                id = 1,
                firstName = "Sajjad",
                learns = listOf("En, Ge"),
                natives = listOf("Fa"),
                pictureUrl = "some url",
                referenceCnt = 10,
                topic = "Android"
            )
        )

        val response = Response.success(
            CommunityMemberResponseDto(
                communityMemberDto = members
            )
        )

        coEvery { apiService.getCommunity(1) } returns response

        val result = dataSource.getCommunityMembers(1)

        assertTrue(result is Result.Success)
        assertEquals(members, (result as Result.Success).data)
    }

    @Test
    fun `getCommunityMembers returns empty list when response code is 404`() = runTest {
        val response = Response.error<CommunityMemberResponseDto>(
            404,
            "{}".toResponseBody("application/json".toMediaType())
        )

        coEvery { apiService.getCommunity(1) } returns response

        val result = dataSource.getCommunityMembers(1)

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
    }

    @Test
    fun `getCommunityMembers returns error when response code is not successful and not 404`() =
        runTest {
            val response = Response.error<CommunityMemberResponseDto>(
                500,
                "{}".toResponseBody("application/json".toMediaType())
            )

            coEvery { apiService.getCommunity(1) } returns response

            val result = dataSource.getCommunityMembers(1)

            assertTrue(result is Result.Error)

            val error = (result as Result.Error).exception
            assertTrue(error is IllegalStateException)
            assertTrue(error.message!!.contains("500"))
        }

    @Test
    fun `getCommunityMembers returns NetworkException when IOException is thrown`() = runTest {
        coEvery { apiService.getCommunity(1) } throws IOException("Network error")

        val result = dataSource.getCommunityMembers(1)

        assertTrue(result is Result.Error)

        val error = (result as Result.Error).exception
        assertTrue(error is NetworkException)
        assertTrue(error.cause is IOException)
    }

    @Test
    fun `getCommunityMembers returns UnexpectedException when unknown exception is thrown`() =
        runTest {
            coEvery { apiService.getCommunity(1) } throws RuntimeException("Something went wrong")

            val result = dataSource.getCommunityMembers(1)

            assertTrue(result is Result.Error)

            val error = (result as Result.Error).exception
            assertTrue(error is UnexpectedException)
            assertTrue(error.cause is RuntimeException)
        }
}