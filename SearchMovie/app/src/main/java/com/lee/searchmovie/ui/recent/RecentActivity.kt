package com.lee.searchmovie.ui.recent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.lee.searchmovie.R
import com.lee.searchmovie.common.Utils
import com.lee.searchmovie.common.base.BaseActivity
import com.lee.searchmovie.common.interfaces.OnItemClickListener
import com.lee.searchmovie.common.wrapper.GridLayoutManagerWrapper
import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.databinding.ActivityRecentBinding
import com.lee.searchmovie.ui.recent.adapter.RecentKeywordAdapter
import com.lee.searchmovie.ui.recent.viewmodel.RecentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 최근 검색어 화면
 * **/
private const val TAG = "RecentActivity"
@AndroidEntryPoint
class RecentActivity : BaseActivity<ActivityRecentBinding>(R.layout.activity_recent) {
    private val viewModel : RecentViewModel by viewModels()
    private lateinit var recentKeywordAdapter: RecentKeywordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            recentActivity = this@RecentActivity
        }
        initRecyclerView()
        viewModel.getRecentKeyword()
    }

    /**
     * LiveData 관찰 함수
     * **/
    override fun observeData() {
        with(viewModel){
            recentKeywordList.observe(this@RecentActivity){ // 저장된 검색어 목록
                Log.d(TAG, "observeData: $it")
                if(it.isEmpty()){
                  with(binding){
                      noDataLayout.visibility = View.VISIBLE
                      recentKeywordRV.visibility = View.INVISIBLE
                  }
                } else {
                    with(binding){
                        noDataLayout.visibility = View.GONE
                        recentKeywordRV.visibility = View.VISIBLE
                    }
                    if(::recentKeywordAdapter.isInitialized){
                        val list = mutableListOf<RecentKeywordEntity>()
                        list.addAll(it)
                        recentKeywordAdapter.submitList(list)
                    }
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
        with(binding){
            deleteAllTV.setOnClickListener {
                viewModel.deleteAllItem()
            }
        }
    }

    private fun initRecyclerView() {
        recentKeywordAdapter = RecentKeywordAdapter()
        recentKeywordAdapter.apply {
            setRemoveButtonClickListener(RemoveItemClickListener(viewModel))
            setOnItemClickListener(ItemClickListener())
        }
        binding.recentKeywordRV.run {
            adapter = recentKeywordAdapter
            layoutManager = GridLayoutManagerWrapper(this@RecentActivity , 2)
        }
    }

    private class RemoveItemClickListener(private val viewModel : RecentViewModel) : OnItemClickListener {
        override fun onClick(view: View, data: Any, position: Int) {
            if(data is RecentKeywordEntity){
                viewModel.deleteItem(data)
            }
        }
    }

    private inner class ItemClickListener : OnItemClickListener {
        override fun onClick(view: View, data: Any, position: Int) {
            if(data is RecentKeywordEntity){
                with(Intent(Utils.ACTION_SEARCH_KEYWORD)){
                    putExtra(Utils.EXTRA_SELECTED_KEYWORD , data.keyword)
                    sendBroadcast(this)
                    finish()
                }
            }
        }
    }
}