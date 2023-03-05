package com.lee.searchmovie.ui.recent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository: MainRepository
) : ViewModel() {
    private val _recentKeywordList = MutableLiveData<MutableList<RecentKeywordEntity>>()
    val recentKeywordList : LiveData<MutableList<RecentKeywordEntity>>
    get() = _recentKeywordList

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

    fun getRecentKeyword() {
        setIsProgress(true)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.getRecentKeyword()
            }
            _recentKeywordList.value = result
            setIsProgress(false)
        }
    }

}