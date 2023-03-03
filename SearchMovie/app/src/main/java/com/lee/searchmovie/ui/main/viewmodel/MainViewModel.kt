package com.lee.searchmovie.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.searchmovie.BuildConfig
import com.lee.searchmovie.data.model.remote.MovieDTO
import com.lee.searchmovie.data.model.remote.MovieResultDTO
import com.lee.searchmovie.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    fun setSearchResult(result : MovieResultDTO){
        _searchResult.value = result
    }

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
                        setSearchResult(result)
                    }
                    setIsProgress(false)
                } else { // 검색 실패
                    Log.d(TAG, "searchMovie: fail searching movie${response.code()}")
                    setIsProgress(false)
                }
            }
        }
    }
}