package com.sajjadfatehi.tandemcommunity.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityMemberResponseDto(
    @SerialName("errorCode")
    val errorCode: Int? = null,
    @SerialName("response")
    val communityMemberDto: List<CommunityMemberDto>? = null,
    @SerialName("type")
    val type: String? = null
)