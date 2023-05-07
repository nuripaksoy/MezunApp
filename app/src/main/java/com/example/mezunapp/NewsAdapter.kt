package com.example.mezunapp

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsAdapter(private val newsList : ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var onClickListener: NewsAdapter.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_list_item,parent,false)
        return NewsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.heading.text = currentItem.heading
        holder.publishDate.text = currentItem.publishDate
        Picasso.get().load(currentItem.imageURL).into(holder.titleImage);

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem)
            }
        }
    }

    class NewsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titleImage : ImageView = itemView.findViewById(R.id.news_list_item_image)
        val publishDate : TextView = itemView.findViewById(R.id.news_list_item_date)
        val heading : TextView = itemView.findViewById(R.id.news_list_item_title)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: News)
    }
}