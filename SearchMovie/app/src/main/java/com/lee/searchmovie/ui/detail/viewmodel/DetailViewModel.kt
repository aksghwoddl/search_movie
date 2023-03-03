package com.lee.searchmovie.ui.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lee.searchmovie.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 상세화면의 ViewModel
 * **/
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _receivedUrl = MutableLiveData<String>()
    val receivedUrl : LiveData<String>
    get() = _receivedUrl
    fun setReceivedUrl(url : String){
        _receivedUrl.value = url
    }

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress : LiveData<Boolean>
        get() = _isProgress
    fun setIsProgress(on : Boolean) {
        _isProgress.value = on
    }
}