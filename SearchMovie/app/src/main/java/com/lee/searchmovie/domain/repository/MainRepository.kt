package com.lee.searchmovie.domain.repository

import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.data.model.remote.MovieResultDTO
import retrofit2.Response

/**
 * Repository Interface
 * **/
interface MainRepository {
    /**
     * 영화 검색하기
     * - id : Naver Client ID
     * - clientKey : Naver Client Key
     * - query : 검색어
     * - page : 검색 시작 페이지
     * **/
    suspend fun searchMovie(
        id : String ,
        clientKey : String ,
        query : String ,
        page : Int
    ) : Response<MovieResultDTO>

    /**
     * 최근 검색어 저장하기
     * - recentKeywordEntity : 저장할 최근 검색어 Entity
     * **/
    suspend fun insertRecentKeyword(
        recentKeywordEntity: RecentKeywordEntity
    )

    /**
     * 최근 검색어 불러오기
     * **/
    suspend fun getRecentKeyword() : MutableList<RecentKeywordEntity>

    /**
     * 최근 검색어 삭제 하기
     * - recentKeywordEntity : 삭제할 칼럼 정보
     * **/
    suspend fun deleteRecentKeyword(recentKeywordEntity: RecentKeywordEntity)
}