package com.sajjadfatehi.tandemcommunity.data.remote.api

import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CommunityApiService {

    @GET(value = "community_{page}.json")
    suspend fun getCommunity(@Path("page") page: Int): Response<CommunityResponseDto>
}