package com.lee.searchmovie.common

import android.content.Context
import android.net.ConnectivityManager
import android.os.IBinder
import android.view.inputmethod.InputMethodManager

/**
 * 본 앱에서 common하게 사용할 Util들을 모아놓은 class
 * **/
class Utils {
    companion object{
        const val EXTRA_MOVIE_URL = "movie_url"
        const val EXTRA_SELECTED_KEYWORD = "selected_keyword"
        const val ACTION_SEARCH_KEYWORD = "com.lee.searchmovie.common.Utils.ACTION_SEARCH_KEYWORD"

        /**
         * 키패드 숨기는 함수
         * - context : 전달받을 Context
         * - token : 키패드를 닫을 View의 Token
         * **/
        fun hideSoftInputKeyboard(context : Context, token : IBinder?) {
            token?.let {
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(token  , 0)
            }
        }

        /**
         * 네트워크 상태 체크하는 함수
         * - context : 전달받을 context
         * **/
        fun checkNetworkConnection(context : Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetwork
            return networkInfo.toString() != "null"
        }
    }
}