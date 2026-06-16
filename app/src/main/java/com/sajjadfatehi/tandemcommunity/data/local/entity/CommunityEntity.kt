package com.sajjadfatehi.tandemcommunity.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_community_members")
data class LikedCommunityMemberEntity(
    @PrimaryKey val memberId: Int
)
