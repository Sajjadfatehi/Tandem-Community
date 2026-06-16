package com.sajjadfatehi.tandemcommunity.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sajjadfatehi.tandemcommunity.data.local.dao.CommunityMemberDao
import com.sajjadfatehi.tandemcommunity.data.local.entity.LikedCommunityMemberEntity

@Database(
    entities = [
        LikedCommunityMemberEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class CommunityDatabase : RoomDatabase() {
    abstract fun communityMemberDao(): CommunityMemberDao
}
