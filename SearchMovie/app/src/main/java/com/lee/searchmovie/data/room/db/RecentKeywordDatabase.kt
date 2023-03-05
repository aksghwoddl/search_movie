package com.lee.searchmovie.data.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.data.room.TypeConverter
import com.lee.searchmovie.data.room.dao.RecentDAO

/**
 * 최근 검색어의 Room DB class
 * **/
@Database(entities = [RecentKeywordEntity::class] , exportSchema = false , version = 1)
@TypeConverters(TypeConverter::class)
abstract class RecentKeywordDatabase : RoomDatabase() {
    abstract fun recentDao() : RecentDAO
}