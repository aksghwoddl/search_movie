package com.lee.searchmovie.ui.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.lee.searchmovie.R
import com.lee.searchmovie.common.Utils
import com.lee.searchmovie.common.base.BaseActivity
import com.lee.searchmovie.databinding.ActivityDetailBinding
import com.lee.searchmovie.ui.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 상세 화면 Activity
 * **/

private const val TAG = "DetailActivity"
@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {
    private val viewModel : DetailViewModel by viewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.movieUrlPage.apply {
            webViewClient = DetailWebViewClient(viewModel)
            settings.javaScriptEnabled = true
        }
        val url = intent.getStringExtra(Utils.EXTRA_MOVIE_URL)
        url?.let {
            viewModel.setReceivedUrl(it)
        }
        this.onBackPressedDispatcher.addCallback(BackPressCallBack())
    }

    /**
     * LiveData 관찰 함수
     * **/
    override fun observeData() {
        with(viewModel){
            receivedUrl.observe(this@DetailActivity){ // 전달받은 URL
                binding.movieUrlPage.loadUrl(it)
            }

            isProgress.observe(this@DetailActivity){ // Task 진행 상태
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

    }

    /**
     * WebViewClient 페이지 Loading 여부를 확인하기 위해 CallBack 함수 재정의
     * **/
    private class DetailWebViewClient(private val viewModel : DetailViewModel) : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            viewModel.setIsProgress(true)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.setIsProgress(false)
        }
    }

    /**
     * 뒤로가기 관련 CallBack
     * - WebView에 아직 페이지가 남아있으면 뒤로가기 실행 그렇지 않으면 액티비티 종료
     * **/
    private inner class BackPressCallBack : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            binding.movieUrlPage.run {
                if(canGoBack()){
                    this.goBack()
                } else {
                    finish()
                }
            }
        }
    }
}