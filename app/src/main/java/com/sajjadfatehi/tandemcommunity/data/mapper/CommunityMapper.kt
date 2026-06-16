package com.sajjadfatehi.tandemcommunity.data.mapper

import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember
import kotlin.collections.orEmpty

fun CommunityMemberDto.toCommunityMember() = CommunityMember(
    id = id,
    firstName = firstName.orEmpty(),
    learns = learns.orEmpty(),
    natives = natives.orEmpty(),
    pictureUrl = pictureUrl.orEmpty(),
    referenceCnt = referenceCnt ?: -1,
    topic = topic.orEmpty(),
    isNew = referenceCnt == 0,
    isLiked = false
)