package com.sajjadfatehi.tandemcommunity.data.remote

import com.sajjadfatehi.tandemcommunity.core.Result
import com.sajjadfatehi.tandemcommunity.data.remote.api.CommunityApiService
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import java.io.IOException
import javax.inject.Inject

class CommunityRemoteDataSourceImpl @Inject constructor(
    private val apiService: CommunityApiService
) : CommunityRemoteDataSource {

    override suspend fun getCommunityMembers(
        page: Int
    ): Result<List<CommunityMemberDto>> = try {
        val response = apiService.getCommunity(page)

        when {
            response.isSuccessful -> {
                Result.Success(
                    response.body()?.communityMemberDto.orEmpty()
                )
            }

            // Backend returns 404 instead of an empty page when there are no
            // more items to load. Treat it as the end of pagination.
            response.code() == 404 -> {
                Result.Success(emptyList())
            }

            else -> {
                Result.Error(
                    IllegalStateException(
                        "Community request failed. HTTP ${response.code()}"
                    )
                )
            }
        }
    } catch (e: IOException) {
        Result.Error(NetworkException("No internet or timeout", e))
    } catch (e: Exception) {
        Result.Error(UnexpectedException("Unexpected error", e))
    }
}
