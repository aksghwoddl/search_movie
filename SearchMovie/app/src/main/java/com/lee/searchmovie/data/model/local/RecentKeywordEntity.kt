package com.lee.searchmovie.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.searchmovie.common.DatabaseConst

/**
 * 최근 검색어 Entity
 * **/
@Entity(tableName = DatabaseConst.TABLE_NAME)
data class RecentKeywordEntity(
    @PrimaryKey(autoGenerate = true)
    val index : Int? ,
    val keyword : String ,
)