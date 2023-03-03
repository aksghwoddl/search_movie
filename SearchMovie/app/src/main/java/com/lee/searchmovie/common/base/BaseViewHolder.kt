package com.lee.searchmovie.common.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * RecyclerView에서 사용할 Base ViewHolder
 * **/
abstract class BaseViewHolder(binding : ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    /**
     * data를 bind 해주는 함수
     * **/
    abstract fun bind(data : Any)
}