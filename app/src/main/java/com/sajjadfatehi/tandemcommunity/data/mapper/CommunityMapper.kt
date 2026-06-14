package com.sajjadfatehi.tandemcommunity.data.mapper

import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityMemberEntity
import com.sajjadfatehi.tandemcommunity.data.remote.dto.CommunityMemberDto
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember

fun CommunityMemberDto.toEntity(page: Int, indexInPage: Int) = CommunityMemberEntity(
    id = id,
    firstName = firstName.orEmpty(),
    learns = learns.orEmpty(),
    natives = natives.orEmpty(),
    pictureUrl = pictureUrl.orEmpty(),
    referenceCnt = referenceCnt ?: -1,
    topic = topic.orEmpty(),
    page = page,
    indexInPage = indexInPage
)

fun CommunityMemberEntity.toCommunityMember(isLiked: Boolean) = CommunityMember(
    id = id,
    firstName = firstName,
    learns = learns,
    natives = natives,
    pictureUrl = pictureUrl,
    referenceCnt = referenceCnt,
    topic = topic,
    isNew = referenceCnt == 0,
    isLiked = isLiked
)
