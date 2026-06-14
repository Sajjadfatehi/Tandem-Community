package com.sajjadfatehi.tandemcommunity.data.remote.dto

data class CommunityDto(
    val firstName: String? = null,
    val id: Int? = null,
    val learns: List<String>? = null,
    val natives: List<String>? = null,
    val pictureUrl: String? = null,
    val referenceCnt: Int? = null,
    val topic: String? = null
)