package com.lee.searchmovie.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.lee.searchmovie.R
import com.lee.searchmovie.common.base.BaseActivity
import com.lee.searchmovie.databinding.ActivityMainBinding
import com.lee.searchmovie.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity (영화 검색하기 화면)
 * **/
private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainViewModel =  viewModel
    }

    /**
     * LiveData 관찰 함수
     * **/
    override fun observeData() {
        with(viewModel){
            searchResult.observe(this@MainActivity){ // 검색 결과
                Log.d(TAG, "observeData: $it")
            }

            page.observe(this@MainActivity){ // 페이지
                searchMovie()
            }

            toastMessage.observe(this@MainActivity){
                Toast.makeText(this@MainActivity , it , Toast.LENGTH_SHORT).show()
            }

            isProgress.observe(this@MainActivity){
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Listener 등록 함수
     * **/
    override fun addListeners() {
        with(binding){
            searchButton.setOnClickListener { // 검색 버튼
                if(searchEditText.text.isNotEmpty()){
                    viewModel.setPage(1)
                } else {
                    viewModel.setToastMessage(resources.getString(R.string.input_keyword))
                }
            }
        }
    }
}