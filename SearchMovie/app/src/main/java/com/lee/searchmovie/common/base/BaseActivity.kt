package com.lee.searchmovie.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Base Activity
 * **/
abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResId : Int
) : AppCompatActivity() {
    private var _binding : T? = null
    val binding  get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this , layoutResId)
        observeData()
        addListeners()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * LiveData 관찰 함수
     * **/
    abstract fun observeData()

    /**
     * Listener 등록 함수
     * **/
    abstract fun addListeners()
}