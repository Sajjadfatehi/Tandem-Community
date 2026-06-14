package com.sajjadfatehi.tandemcommunity.data.remote.dto

data class CommunityResponseDto(
    val errorCode: Int? = null,
    val communityDto: List<CommunityDto>? = null,
    val type: String? = null
)