package com.lee.searchmovie.ui.recent

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.lee.searchmovie.R
import com.lee.searchmovie.common.base.BaseActivity
import com.lee.searchmovie.databinding.ActivityRecentBinding
import com.lee.searchmovie.ui.recent.viewmodel.RecentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 최근 검색어 화면
 * **/
private const val TAG = "RecentActivity"
@AndroidEntryPoint
class RecentActivity : BaseActivity<ActivityRecentBinding>(R.layout.activity_recent) {
    private val viewModel : RecentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            recentActivity = this@RecentActivity
        }
        viewModel.getRecentKeyword()
    }

    /**
     * LiveData 관찰 함수
     * **/
    override fun observeData() {
        with(viewModel){
            recentKeywordList.observe(this@RecentActivity){ // 저장된 검색어 목록
                Log.d(TAG, "observeData: $it")
            }

            isProgress.observe(this@RecentActivity){ // Task 진행상태
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }

            toastMessage.observe(this@RecentActivity){ // Toast Message
                Toast.makeText(this@RecentActivity , it , Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Listener 등록 함수
     * **/
    override fun addListeners() {

    }
}