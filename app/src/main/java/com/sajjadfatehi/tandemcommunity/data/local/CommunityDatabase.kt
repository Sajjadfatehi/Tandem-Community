package com.sajjadfatehi.tandemcommunity.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sajjadfatehi.tandemcommunity.data.local.converter.StringListTypeConverter
import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityMemberDao
import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityRemoteKeyDao
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityMemberEntity
import com.sajjadfatehi.tandemcommunity.data.local.entity.CommunityRemoteKeyEntity
import com.sajjadfatehi.tandemcommunity.data.local.entity.LikedCommunityMemberEntity

@Database(
    entities = [
        CommunityMemberEntity::class,
        LikedCommunityMemberEntity::class,
        CommunityRemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListTypeConverter::class)
abstract class CommunityDatabase : RoomDatabase() {
    abstract fun communityMemberDao(): CommunityMemberDao
    abstract fun communityRemoteKeyDao(): CommunityRemoteKeyDao
}
