package com.lee.searchmovie.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.searchmovie.BuildConfig
import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.data.model.remote.MovieDTO
import com.lee.searchmovie.data.model.remote.MovieResultDTO
import com.lee.searchmovie.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword : LiveData<String>
    get() = _searchKeyword
    fun setSearchKeyword(keyword : String) {
        _searchKeyword.value = keyword
    }

    private val _searchResult = MutableLiveData<MovieResultDTO>()
    val searchResult : LiveData<MovieResultDTO>
    get() = _searchResult

    private val _movieList = MutableLiveData<ArrayList<MovieDTO>>()
    val movieList : LiveData<ArrayList<MovieDTO>>
    get() = _movieList
    fun setMovieList(list : ArrayList<MovieDTO>){
        _movieList.value = list
    }

    private val _page = MutableLiveData<Int>()
    val page : LiveData<Int>
    get() = _page
    fun setPage(page : Int){
        _page.value = page
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
    get() = _toastMessage
    fun setToastMessage(message : String){
        _toastMessage.value = message
    }

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
    get() = _isProgress
    fun setIsProgress(on : Boolean) {
        _isProgress.value = on
    }

    /**
     * 영화 검색하기
     * **/
    fun searchMovie() {
        viewModelScope.launch {
            setIsProgress(true)
            searchKeyword.value?.let { keyword ->
                val response = repository.searchMovie(
                    BuildConfig.NAVER_CLIENT_ID ,
                    BuildConfig.NAVER_CLIENT_SECRET ,
                    keyword ,
                    page.value!!
                )
                if(response.isSuccessful){ // 검색 성공
                    response.body()?.let { result ->
                       _searchResult.value = result
                    }
                } else { // 검색 실패
                    Log.d(TAG, "searchMovie: fail searching movie${response.code()}")
                    setIsProgress(false)
                }
            }
        }
    }

    /**
     * 검색어 저장하기
     * **/
    fun saveRecentKeyword() {
        searchKeyword.value?.let {
            if(it.isNotEmpty()){ // 검색어가 빈 텍스트가 아닐때만 저장
                viewModelScope.launch(Dispatchers.IO) {
                    val keywordList = repository.getRecentKeyword()
                    val searchKeywordFlow = keywordList.asFlow().filter { recentKeyword -> // 키워드를 검색하는 Flow 생성
                        recentKeyword.keyword == it
                    }

                    searchKeywordFlow.collect{ keyword -> // 현재 검색한 키워드와 같은 키워드가 이미 저장되어있다면 해당 키워드는 삭제
                        repository.deleteRecentKeyword(keyword)
                    }
                    val saveKeyword = RecentKeywordEntity(null , it)
                    repository.insertRecentKeyword(saveKeyword) // 키워드 저장
                }
            }
        }
    }
}