package com.lee.searchmovie.data.room.dao

import androidx.room.*
import com.lee.searchmovie.common.DatabaseConst
import com.lee.searchmovie.data.model.local.RecentKeywordEntity

/**
 * Room을 control하는 DAO
 * **/
@Dao
interface RecentDAO {

    /**
     * 즐겨찾기 단어를 데이터베이스에 기록하는 함수
     * **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentKeyword(recentKeyword: RecentKeywordEntity)

    /**
     * 최근 기록된 상위 10개의 최근 검색어를 불러오는 함수
     * **/
    @Query("SELECT * FROM ${DatabaseConst.TABLE_NAME}")
    suspend fun getRecentKeyword() : MutableList<RecentKeywordEntity>

    /**
     * 최근 검색어를 업데이트 하는 함수
     * **/
    @Delete
    suspend fun deleteRecentKeyword(recentKeyword: RecentKeywordEntity)

}