package com.lee.searchmovie.ui.recent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lee.searchmovie.common.base.BaseViewHolder
import com.lee.searchmovie.common.interfaces.OnItemClickListener
import com.lee.searchmovie.data.model.local.RecentKeywordEntity
import com.lee.searchmovie.databinding.RecentKeywordItemBinding

/**
 * 최근 검색어 Adapter Class
 * **/
class RecentKeywordAdapter : ListAdapter<RecentKeywordEntity ,RecentKeywordAdapter.RecentKeywordViewHolder>(DiffUtilCallBack()) {

    private var removeButtonClickListener : OnItemClickListener? = null
    private var onItemClickListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentKeywordViewHolder {
        val binding = RecentKeywordItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return RecentKeywordViewHolder(binding , onItemClickListener!! , removeButtonClickListener!!)
    }

    override fun onBindViewHolder(holder: RecentKeywordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setRemoveButtonClickListener(listener: OnItemClickListener) {
        removeButtonClickListener = listener
    }

    class RecentKeywordViewHolder(
        private val binding : RecentKeywordItemBinding ,
        private val onItemClickListener: OnItemClickListener ,
        private val onRemoveClickListener : OnItemClickListener
    ) : BaseViewHolder(binding){
        /**
         * data를 bind 해주는 함수
         * **/
        override fun bind(data: Any) {
            if(data is RecentKeywordEntity){
                with(binding){
                    keywordTV.text = data.keyword
                }
                addListener(data)
            }
        }

        private fun addListener(data : RecentKeywordEntity) {
            val position = adapterPosition
            if(adapterPosition != RecyclerView.NO_POSITION){
                binding.deleteButton.setOnClickListener { // 삭제 버튼
                    onRemoveClickListener.onClick(it , data , position)
                }
                itemView.setOnClickListener { // 아이템 클릭
                    onItemClickListener.onClick(it , data , position)
                }
            }
        }
    }

    private class DiffUtilCallBack : DiffUtil.ItemCallback<RecentKeywordEntity>() {
        override fun areItemsTheSame(
            oldItem: RecentKeywordEntity,
            newItem: RecentKeywordEntity
        ): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(
            oldItem: RecentKeywordEntity,
            newItem: RecentKeywordEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}