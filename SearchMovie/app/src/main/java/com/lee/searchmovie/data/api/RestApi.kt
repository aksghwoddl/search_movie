package com.lee.searchmovie.data.api

import com.lee.searchmovie.common.NetworkConst
import com.lee.searchmovie.data.model.remote.MovieResultDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Rest Api
 * **/
interface RestApi {
    /**
     * 영화 검색하기
     * **/
    @GET(NetworkConst.SEARCH_MOVIE_URL)
    suspend fun searchMovie(
        @Header(NetworkConst.NAVER_CLIENT_ID) id : String ,
        @Header(NetworkConst.NAVER_CLIENT_SECERET_KEY) secretKey : String ,
        @Query(NetworkConst.QUERY) query : String ,
        @Query(NetworkConst.START) page : Int ,
        @Query(NetworkConst.DISPLAY) display : Int
    ) : Response<MovieResultDTO>
}