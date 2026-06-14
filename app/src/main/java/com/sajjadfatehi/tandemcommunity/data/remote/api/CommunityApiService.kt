package com.sajjadfatehi.tandemcommunity.data.remote.api

import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CommunityApiService {

    @GET(value = "community_{page}.json")
    suspend fun getCommunity(@Path("page") page: Int): Response<CommunityMemberResponseDto>
}