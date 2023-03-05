package com.lee.searchmovie.ui.main.adapter

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lee.searchmovie.R
import com.lee.searchmovie.common.base.BaseViewHolder
import com.lee.searchmovie.common.interfaces.OnItemClickListener
import com.lee.searchmovie.data.model.remote.MovieDTO
import com.lee.searchmovie.databinding.SearchResultItemBinding

/**
 * 검색 결과 RecyclerViewAdapter
 * **/
private const val EMPTY_IMAGE = ""
private const val TAG = "SearchResultRecyclerAdapter"

class SearchResultRecyclerAdapter : ListAdapter<MovieDTO, SearchResultRecyclerAdapter.SearchResultViewHolder>(DiffUtilCallBack()) {
    private lateinit var onItemClickListener : OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = SearchResultItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return SearchResultViewHolder(binding , onItemClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchResultViewHolder(
        private val binding : SearchResultItemBinding ,
        private val onItemClickListener: OnItemClickListener
    ) : BaseViewHolder(binding) {
        override fun bind(data : Any) {
            if(data is MovieDTO){
               with(binding){
                   setPosterImage(data.image)
                   movieNameTV.run { // 영화 제목
                       text = parsingHtmlData(data.title)
                       isSelected = true // 마퀴 동작을 위한 Focus
                   }
                   releaseDateTV.text = String.format(binding.root.resources.getString(R.string.release_date) , data.pubDate) // 출시일
                   ratingTV.text = String.format(binding.root.resources.getString(R.string.rating) , data.userRating) // 평점
               }
                addListeners(data)
            }
        }
        /**
         * Html을 파싱하는 함수
         * - data : 파싱할 Html text
         * **/
        private fun parsingHtmlData(data : String) : Spanned {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){ // API level 24이상
                Html.fromHtml(data , Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(data)
            }
        }

        /**
         * 전달받은 URI를 기반으로 Glide를 통해 이미지를 setting
         * - image : 전달받은 이미지 URI
         * **/
        private fun setPosterImage(image : String){
            if(image == EMPTY_IMAGE){
                Log.d(TAG, "bind: image is empty!!")
                Glide.with(binding.root)
                    .load(R.drawable.no_image)
                    .into(binding.posterIV)
            } else {
                Glide.with(binding.root)
                    .load(image)
                    .error(R.drawable.no_image)
                    .into(binding.posterIV)
            }
        }

        private fun addListeners(data : MovieDTO) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    onItemClickListener.onClick(it , data , position)
                }
            }
        }
    }

    private class DiffUtilCallBack : DiffUtil.ItemCallback<MovieDTO>(){
        override fun areItemsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem == newItem
        }
    }
}