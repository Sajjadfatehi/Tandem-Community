package com.sajjadfatehi.tandemcommunity.domain.datasource

import com.sajjadfatehi.tandemcommunity.core.Result
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto

interface CommunityRemoteDataSource {
    suspend fun getCommunityMembers(page: Int): Result<List<CommunityMemberDto>>
}
