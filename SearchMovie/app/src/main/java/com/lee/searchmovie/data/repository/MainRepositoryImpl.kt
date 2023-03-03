package com.lee.searchmovie.data.repository

import com.lee.searchmovie.common.NetworkConst
import com.lee.searchmovie.data.api.RestApi
import com.lee.searchmovie.domain.repository.MainRepository
import javax.inject.Inject

/**
 * Repository의 구현부
 * **/
class MainRepositoryImpl @Inject constructor(
    private val restApi: RestApi
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

}