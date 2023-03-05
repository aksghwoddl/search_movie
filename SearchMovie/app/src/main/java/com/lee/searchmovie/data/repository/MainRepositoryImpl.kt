package com.lee.searchmovie.data.repository

import com.lee.searchmovie.common.NetworkConst
import com.lee.searchmovie.data.api.RestApi
import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.data.room.dao.RecentDAO
import com.lee.searchmovie.domain.repository.MainRepository
import javax.inject.Inject

/**
 * Repository의 구현부
 * **/
class MainRepositoryImpl @Inject constructor(
    private val restApi: RestApi ,
    private val recentDAO: RecentDAO
) : MainRepository {
    /**
     * 영화 검색하기
     * - id : Naver Client ID
     * - clientKey : Naver Client Key
     * - query : 검색어
     * - page : 검색 시작 페이지
     * **/
    override suspend fun searchMovie(
        id: String,
        clientKey: String,
        query: String,
        page: Int
    ) = restApi.searchMovie(id , clientKey , query , page , NetworkConst.DISPLAY_PAGE_VALUE)

    /**
     * 최근 검색어 저장하기
     * - recentKeywordEntity : 저장할 최근 검색어 Entity
     * **/
    override suspend fun insertRecentKeyword(recentKeywordEntity: RecentKeywordEntity) = recentDAO.insertRecentKeyword(recentKeywordEntity)

    /**
     * 최근 검색어 불러오기
     * **/
    override suspend fun getRecentKeyword(): MutableList<RecentKeywordEntity> = recentDAO.getRecentKeyword()

    /**
     * 최근 검색어 삭제 하기
     * - recentKeywordEntity : 삭제할 칼럼 정보
     * **/
    override suspend fun deleteRecentKeyword(recentKeywordEntity: RecentKeywordEntity) = recentDAO.deleteRecentKeyword(recentKeywordEntity)

    /**
     * 모든 최근 검색어 삭제하기
     * **/
    override suspend fun deleteAllRecentKeyword() = recentDAO.deleteAllRecentKeyword()

}