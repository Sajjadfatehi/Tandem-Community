package com.sajjadfatehi.tandemcommunity.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_members")
data class CommunityMemberEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val learns: List<String>,
    val natives: List<String>,
    val pictureUrl: String,
    val referenceCnt: Int,
    val topic: String,
    val page: Int,
    val indexInPage: Int
)

@Entity(tableName = "liked_community_members")
data class LikedCommunityMemberEntity(
    @PrimaryKey val memberId: Int
)

@Entity(tableName = "community_remote_keys")
data class CommunityRemoteKeyEntity(
    @PrimaryKey val memberId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
