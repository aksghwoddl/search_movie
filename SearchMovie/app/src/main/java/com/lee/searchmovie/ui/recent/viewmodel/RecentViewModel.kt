package com.lee.searchmovie.ui.recent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.searchmovie.R
import com.lee.searchmovie.common.provider.ResourceProvider
import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 최근 검색어 ViewModel
 * **/
@HiltViewModel
class RecentViewModel @Inject constructor(
    private val repository: MainRepository ,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _recentKeywordList = MutableLiveData<MutableList<RecentKeywordEntity>>()
    val recentKeywordList : LiveData<MutableList<RecentKeywordEntity>>
    get() = _recentKeywordList

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage : LiveData<String>
    get() = _toastMessage

    /**
     * 최근 검색어 불러오기
     * **/
    fun getRecentKeyword() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.getRecentKeyword()
            }
            _recentKeywordList.value = result
        }
    }

    /**
     * 검색어 지우기
     * - recentKeywordEntity : 선택된 아이템의 data
     * **/
    fun deleteItem(recentKeywordEntity: RecentKeywordEntity) {
        viewModelScope.launch{
            val result = withContext(Dispatchers.IO){
                with(repository){
                    deleteRecentKeyword(recentKeywordEntity)
                    getRecentKeyword()
                }
            }
            _recentKeywordList.value = result
        }
    }

    fun deleteAllItem() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                with(repository){
                    deleteAllRecentKeyword()
                    getRecentKeyword()
                }
            }
            _recentKeywordList.value = result
            _toastMessage.value = resourceProvider.getString(R.string.success_all_delete)
        }
    }

}