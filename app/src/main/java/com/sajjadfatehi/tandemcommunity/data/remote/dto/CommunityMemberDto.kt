package com.sajjadfatehi.tandemcommunity.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityMemberDto(
    @SerialName("firstName")
    val firstName: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("learns")
    val learns: List<String>? = null,
    @SerialName("natives")
    val natives: List<String>? = null,
    @SerialName("pictureUrl")
    val pictureUrl: String? = null,
    @SerialName("referenceCnt")
    val referenceCnt: Int? = null,
    @SerialName("topic")
    val topic: String? = null
)