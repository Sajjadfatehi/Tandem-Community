package com.sajjadfatehi.tandemcommunity.data.remote

import com.sajjadfatehi.tandemcommunity.core.Result
import com.sajjadfatehi.tandemcommunity.data.remote.api.CommunityApiService
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import javax.inject.Inject

class CommunityRemoteDataSourceImpl @Inject constructor(
    private val apiService: CommunityApiService
) : CommunityRemoteDataSource {

    override suspend fun getCommunityMembers(page: Int): Result<List<CommunityMemberDto>> {
        return try {
            val response = apiService.getCommunity(page)

            when {
                response.isSuccessful -> Result.Success(response.body()?.communityMemberDto.orEmpty())
                response.code() == 404 -> Result.Success(emptyList())
                else -> Result.Error(IllegalStateException("Community request failed"))
            }
        } catch (e: Exception) {
            if (e is java.io.IOException) {
                Result.Error(e)
            } else {
                Result.Success(emptyList())
            }
        }
    }
}
