package com.example.mezunapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.Shapeable
import com.squareup.picasso.Picasso

class AlumniAdapter(private val userList : ArrayList<User>) : RecyclerView.Adapter<AlumniAdapter.AlumniViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class AlumniViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userImage: ShapeableImageView = itemView.findViewById(R.id.alumniListItemImage)
        val userText: TextView = itemView.findViewById(R.id.alumniListItemTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumniViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.alumni_list_item,parent,false)
        return AlumniViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: User)
    }

    override fun onBindViewHolder(holder: AlumniViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.userText.text = currentItem.firstName +" "+currentItem.lastName
        Picasso.get().load(currentItem.imageURL).into(holder.userImage);


        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem)
            }
        }
    }
}
