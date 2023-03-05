package com.lee.searchmovie.data.room

import com.google.gson.Gson
import com.lee.searchmovie.data.model.local.RecentKeywordEntity

/**
 * Room의 data를 converting하기위한 TypeConverter class
 * **/
class TypeConverter {
    @androidx.room.TypeConverter
    fun recentKeywordToJson(recentKeyword : RecentKeywordEntity) : String = Gson().toJson(recentKeyword)

    @androidx.room.TypeConverter
    fun jsonToRecentKeyword(json : String) : RecentKeywordEntity = Gson().fromJson(json , RecentKeywordEntity::class.java)
}