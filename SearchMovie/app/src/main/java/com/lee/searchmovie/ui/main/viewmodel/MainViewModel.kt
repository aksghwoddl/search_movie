package com.lee.searchmovie.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.searchmovie.BuildConfig
import com.lee.searchmovie.R
import com.lee.searchmovie.common.provider.ResourceProvider
import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.data.model.remote.MovieDTO
import com.lee.searchmovie.data.model.remote.MovieResultDTO
import com.lee.searchmovie.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository ,
    private val resourceProvider: ResourceProvider
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

    private val exceptionHandler = CoroutineExceptionHandler{ _ , exception ->
        when(exception){
            is SocketTimeoutException -> {
                _toastMessage.value = resourceProvider.getString(R.string.socket_timeout)
                _isProgress.value = false
            }
        }
    }

    /**
     * ?????? ????????????
     * **/
    fun searchMovie() {
        viewModelScope.launch(exceptionHandler) {
            setIsProgress(true)
            searchKeyword.value?.let { keyword ->
                val response = repository.searchMovie(
                    BuildConfig.NAVER_CLIENT_ID ,
                    BuildConfig.NAVER_CLIENT_SECRET ,
                    keyword ,
                    page.value!!
                )
                if(response.isSuccessful){ // ?????? ??????
                    response.body()?.let { result ->
                       _searchResult.value = result
                    }
                } else { // ?????? ??????
                    Log.d(TAG, "searchMovie: fail searching movie${response.code()}")
                    resourceProvider.getString(R.string.fail_response)
                    setIsProgress(false)
                }
            }
        }
    }

    /**
     * ????????? ????????????
     * **/
    fun saveRecentKeyword() {
        searchKeyword.value?.let {
            if(it.isNotEmpty()){ // ???????????? ??? ???????????? ???????????? ??????
                viewModelScope.launch(Dispatchers.IO) {
                    repository.getRecentKeyword().asFlow().filter { recentKeyword -> // ???????????? ???????????? Flow ??????
                        recentKeyword.keyword == it
                    }.collect { keyword -> // ?????? ????????? ???????????? ?????? ???????????? ?????? ????????????????????? ?????? ???????????? ??????
                        repository.deleteRecentKeyword(keyword)
                    }
                    val saveKeyword = RecentKeywordEntity(null , it)
                    repository.insertRecentKeyword(saveKeyword) // ????????? ??????
                }
            }
        }
    }
}