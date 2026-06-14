package com.sajjadfatehi.tandemcommunity.domain.model

data class CommunityMember(
    val id: Int,
    val firstName: String,
    val learns: List<String>,
    val natives: List<String>,
    val pictureUrl: String,
    val referenceCnt: Int,
    val topic: String,
    val isNew: Boolean,
    val isLiked: Boolean = false
)