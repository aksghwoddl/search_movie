package com.lee.searchmovie.common.interfaces

import android.view.View

/**
 * Adapter Item Click Listener Interface
 * - view : 클릭된 뷰
 * - data : 클릭된 뷰가 가지고 있는 data
 * - position : 클릭된 뷰의 position
 * **/
interface OnItemClickListener {
    fun onClick(view : View , data : Any  , position : Int)
}