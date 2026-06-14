package com.sajjadfatehi.tandemcommunity.data.remote

import com.sajjadfatehi.tandemcommunity.data.remote.api.CommunityApiService
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto
import com.sajjadfatehi.tandemcommunity.domain.datasource.CommunityRemoteDataSource
import javax.inject.Inject

class CommunityRemoteDataSourceImpl @Inject constructor(
    private val apiService: CommunityApiService
): CommunityRemoteDataSource {

    override suspend fun getCommunityMembers(page: Int): List<CommunityMemberDto> {
        val response = apiService.getCommunity(page)
        if (!response.isSuccessful) {
            throw IllegalStateException("Community request failed: ${response.code()}")
        }
        return response.body()?.communityMemberDto.orEmpty()
    }
}
