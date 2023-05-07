package com.example.mezunapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ContentAdapter(private val contentList : ArrayList<Content>) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>()  {

    private var onClickListener: ContentAdapter.OnClickListener? = null

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val contentImage: ImageView = itemView.findViewById(R.id.content_list_item_image)
        val contentTitle: TextView = itemView.findViewById(R.id.content_list_item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_content_list_item,parent,false)
        return ContentAdapter.ContentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    fun setOnClickListener(onClickListener: ContentAdapter.OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model:Content)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val currentItem = contentList[position]
        holder.contentTitle.text = currentItem.header
        Picasso.get().load(currentItem.image).into(holder.contentImage);


        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem)
            }
        }
    }
}