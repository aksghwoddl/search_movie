package com.lee.searchmovie.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
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
import com.lee.searchmovie.ui.recent.RecentActivity
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
    private lateinit var searchReceiver: SearchReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            mainViewModel = viewModel
            mainActivity = this@MainActivity
        }
        initRecyclerView()
        initBroadcastReceiver()
    }

    override fun onDestroy() {
        if(::searchReceiver.isInitialized){
            unregisterReceiver(searchReceiver)
        }
        super.onDestroy()
    }

    /**
     * LiveData 관찰 함수
     * **/
    override fun observeData() {
        with(viewModel){
            searchKeyword.observe(this@MainActivity){ // 검색 키워드
                if(it.isEmpty()){ // Clear Button이 눌려 검색 키워드가 비어있을때
                    Log.d(TAG, "observeData: searchKeyword is set empty")
                    val emptyList = arrayListOf<MovieDTO>()
                    addNewList(emptyList)
                } else { // 검색 키워드가 존재 할때
                    setPage(FIRST_PAGE)
                }
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
                            noResultLayout.visibility = View.VISIBLE
                            searchMovieRV.visibility = View.INVISIBLE
                        }
                        setIsProgress(false)
                    } else { // 검색 결과가 존재할때
                        binding.run {
                            noResultLayout.visibility = View.GONE
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
                        if(it.total > currentPage){ // 마지막 페이지 체크
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
            searchEditText.setOnKeyListener { _, keyCode , keyEvent -> // 검색버튼
                when(keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        if(keyEvent.action == KeyEvent.ACTION_UP){ // 두번 실행되는것을 방지
                            searchEditText.text.toString().let { keyword ->
                                if(keyword.isNotEmpty()){ // 검색창이 비어있지 않을때
                                    searchMovie(keyword)
                                } else { // 검색창이 비어있을때
                                    viewModel.setToastMessage(resources.getString(R.string.input_keyword))
                                }
                            }
                        }
                    }
                }
                false
            }

            searchTextInputLayout.setEndIconOnClickListener { // Clear Button
                searchEditText.text.clear()
                viewModel.setSearchKeyword(searchEditText.text.toString())
            }
        }
    }

    private fun initRecyclerView() {
        searchResultRecyclerAdapter = SearchResultRecyclerAdapter()
        searchResultRecyclerAdapter.setOnItemClickListener(ItemClickListener(this@MainActivity))
        binding.searchMovieRV.apply {
            layoutManager = LinearLayoutManagerWrapper(this@MainActivity)
            adapter = searchResultRecyclerAdapter
            addOnScrollListener(ScrollListener(viewModel , this@MainActivity))
            itemAnimator = null // 깜빡임 방지
        }
    }

    private fun initBroadcastReceiver() {
        searchReceiver = SearchReceiver(viewModel , binding)
        val intentFilter = IntentFilter()
        intentFilter.addAction(Utils.ACTION_SEARCH_KEYWORD)
        registerReceiver(searchReceiver , intentFilter)
    }

    /**
     * 영화를 검색하는 함수
     * - keyword : 검색할 단어
     * **/
    private fun searchMovie(keyword : String){
        if(Utils.checkNetworkConnection(this@MainActivity)){ // 네트워크 연결상태 체크
            with(viewModel){
                setSearchKeyword(keyword) // LiveData setting
                saveRecentKeyword() // 검색어 저장하기
            }
        } else { // 네트워크 연결 되어있지 않을때
            viewModel.setToastMessage(getString(R.string.check_network))
            viewModel.setIsProgress(false)
        }
        Utils.hideSoftInputKeyboard(this@MainActivity , binding.root.windowToken) // 키패드 숨기기
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
     * - list : 변경할 새로운 리스트
     * **/
    private fun addNewList(list : ArrayList<MovieDTO>) {
        if(::searchResultRecyclerAdapter.isInitialized){
            searchResultRecyclerAdapter.submitList(list)
        }
        viewModel.setIsProgress(false)
    }

    /**
     * 최근 검색어 화면 실행하기
     * **/
    fun startRecentKeywordActivity() {
        with(Intent(this@MainActivity , RecentActivity::class.java)){
            startActivity(this)
        }
    }

    /**
     * RecyclerView Scroll 리스너
     * **/
    private class ScrollListener(
        private val viewModel : MainViewModel ,
        private val context: Context
    ) : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(Utils.checkNetworkConnection(context)){ // 네트워크 연결 상태일때만 추가 페이지 로딩
                if(!recyclerView.canScrollVertically(RECYCLER_VIEW_BOTTOM)){
                    if(viewModel.isProgress.value!!){ // 아직 로딩이 끝나지 않았음에도 연속으로 페이징 처리가 되는것을 방지
                        Log.d(TAG, "onScrollStateChanged: skip paging , app did not loading result yet")
                    } else {
                        if(viewModel.searchKeyword.value!!.isNotEmpty()){ // 검색 키워드가 비어있지 않을때
                            viewModel.page.value?.let { currentPage ->
                                val newPage = currentPage + NetworkConst.DISPLAY_PAGE_VALUE
                                viewModel.setPage(newPage)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 아이템 클릭 리스너
     * **/
    private class ItemClickListener(private val context : Context) : OnItemClickListener {
        override fun onClick(view: View, data: Any, position: Int) {
            if(Utils.checkNetworkConnection(context)){ // 인터넷 연결 상태
                if(data is MovieDTO){
                    with(Intent( context, DetailActivity::class.java)){
                        putExtra(Utils.EXTRA_MOVIE_URL , data.link)
                        context.startActivity(this)
                    }
                    /*with(Intent(Intent.ACTION_VIEW)){ 브라우저로 열기
                        this.data = Uri.parse(data.link)
                        startActivity(this)
                    }*/
                }
            } else { // 인터넷이 연결되어 있지 않을때
                Toast.makeText(context , context.getString(R.string.check_network) , Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 최근 검색어 선택 후 검색을 하기 위한 Receiver
     * **/
    private class SearchReceiver(
        private val viewModel: MainViewModel ,
        private val binding : ActivityMainBinding
    ) : BroadcastReceiver() {
        override fun onReceive(context : Context?, target : Intent?) {
            when(target?.action){
                Utils.ACTION_SEARCH_KEYWORD -> {
                    Log.d(TAG, "onReceive: ACTION_SEARCH_KEYWORD")
                    val keyword = target.getStringExtra(Utils.EXTRA_SELECTED_KEYWORD)
                    keyword?.let {
                        binding.searchEditText.text = Editable.Factory.getInstance().newEditable(it)
                        viewModel.setSearchKeyword(it)
                    }
                }
            }
        }
    }
}