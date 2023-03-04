package com.lee.searchmovie.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.lee.searchmovie.R
import com.lee.searchmovie.common.NetworkConst
import com.lee.searchmovie.common.Utils
import com.lee.searchmovie.common.base.BaseActivity
import com.lee.searchmovie.common.interfaces.OnItemClickListener
import com.lee.searchmovie.common.wrapper.LinearLayoutManagerWrapper
import com.lee.searchmovie.data.model.remote.MovieDTO
import com.lee.searchmovie.databinding.ActivityMainBinding
import com.lee.searchmovie.ui.detail.DetailActivity
import com.lee.searchmovie.ui.main.adapter.SearchResultRecyclerAdapter
import com.lee.searchmovie.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity (영화 검색하기 화면)
 * **/
private const val TAG = "MainActivity"
private const val FIRST_PAGE = 1
private const val RECYCLER_VIEW_BOTTOM = 1

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel : MainViewModel by viewModels()
    private lateinit var searchResultRecyclerAdapter: SearchResultRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            mainViewModel = viewModel
            mainActivity = this@MainActivity
        }
        initRecyclerView()
    }

    /**
     * LiveData 관찰 함수
     * **/
    override fun observeData() {
        with(viewModel){
            searchKeyword.observe(this@MainActivity){ // 검색 키워드
                setPage(FIRST_PAGE)
            }

            searchResult.observe(this@MainActivity){ // 검색 결과
                if(checkFirstPage()){ // 첫 페이지일 경우
                    setMovieList(it.items)
                } else { // 다음 페이지 부터는 리스트를 누적한다.
                    movieList.value?.let { currentList ->
                        val newList = ArrayList(currentList)
                        newList.addAll(it.items)
                        setMovieList(newList)
                    }
                }
            }

            movieList.observe(this@MainActivity){ // 검색된 영화 목록
                if(checkFirstPage()){ // 첫 페이지일 경우
                    if(it.isEmpty()){ // 검색 결과가 존재하지 않을때
                        binding.run {
                            noResultIV.visibility = View.VISIBLE
                            searchMovieRV.visibility = View.INVISIBLE
                        }
                    } else { // 검색 결과가 존재할때
                        binding.run {
                            noResultIV.visibility = View.GONE
                            searchMovieRV.visibility = View.VISIBLE
                        }
                       addNewList(it)
                    }
                } else { // 이후 페이지일때
                    addNewList(it)
                }
            }

            page.observe(this@MainActivity){ currentPage -> // 검색 페이지
                if(checkFirstPage()){ // 첫번째 페이지일 경우에는 바로 검색
                    searchMovie()
                } else {
                    searchResult.value?.let {
                        if(it.total > currentPage){
                            searchMovie()
                        }
                    }
                }
            }

            toastMessage.observe(this@MainActivity){ // Toast Message
                Toast.makeText(this@MainActivity , it , Toast.LENGTH_SHORT).show()
            }

            isProgress.observe(this@MainActivity){ // Task 진행 상태
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
            searchEditText.setOnKeyListener { _, keyCode , _ ->
                when(keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        searchEditText.text?.let {
                            if(it.isNotEmpty()){
                                viewModel.setSearchKeyword(it.toString())
                            } else {
                                viewModel.setToastMessage(resources.getString(R.string.input_keyword))
                            }
                        }
                    }
                }
                false
            }

            searchTextInputLayout.setEndIconOnClickListener {
                searchEditText.text.clear()
                val emptyList = arrayListOf<MovieDTO>()
                addNewList(emptyList)
            }
        }
    }

    private fun initRecyclerView() {
        searchResultRecyclerAdapter = SearchResultRecyclerAdapter()
        searchResultRecyclerAdapter.setOnItemClickListener(ItemClickListener())
        binding.searchMovieRV.run {
            layoutManager = LinearLayoutManagerWrapper(this@MainActivity)
            adapter = searchResultRecyclerAdapter
            addOnScrollListener(ScrollListener())
            itemAnimator = null // 깜빡임 방지
        }
    }

    /**
     * 현재 페이지가 첫번째 페이지인지 판단하는 함수
     * **/
    private fun checkFirstPage() : Boolean {
        viewModel.page.value?.let {
            return it == FIRST_PAGE
        }
        return false
    }

    /**
     * Adapter에 subList하는 함수
     * **/
    private fun addNewList(list : ArrayList<MovieDTO>) {
        if(::searchResultRecyclerAdapter.isInitialized){
            searchResultRecyclerAdapter.submitList(list)
        }
    }

    /**
     * RecyclerView Scroll 리스너
     * **/
    private inner class ScrollListener : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(RECYCLER_VIEW_BOTTOM)){
                if(viewModel.isProgress.value!!){ // 아직 로딩이 끝나지 않았음에도 연속으로 페이징 처리가 되는것을 방지
                    Log.d(TAG, "onScrollStateChanged: skip paging , app did not loading result yet")
                } else {
                    viewModel.page.value?.let { currentPage ->
                        val newPage = currentPage + NetworkConst.DISPLAY_PAGE_VALUE
                        viewModel.setPage(newPage)
                    }     
                }
            }
        }
    }

    /**
     * 아이템 클릭 리스너
     * **/
    private inner class ItemClickListener : OnItemClickListener {
        override fun onClick(view: View, data: Any, position: Int) {
            if(data is MovieDTO){
                with(Intent(this@MainActivity , DetailActivity::class.java)){
                    putExtra(Utils.EXTRA_MOVIE_URL , data.link)
                    startActivity(this)
                }
                /*with(Intent(Intent.ACTION_VIEW)){ 브라우저로 열기
                    this.data = Uri.parse(data.link)
                    startActivity(this)
                }*/
            }
        }
    }
}