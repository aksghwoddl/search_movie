package com.lee.searchmovie.domain.repository

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
}