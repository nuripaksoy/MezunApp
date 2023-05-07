package com.example.mezunapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class NewsDetailView : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var tvDueDate: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail_view)

        imageView = findViewById(R.id.newsDTO_image)
        tvDueDate = findViewById(R.id.newsDTO_dueDate)
        tvContent = findViewById(R.id.newsDTO_content)
        tvTitle = findViewById(R.id.newsDTO_title)

        var newsList : News?=null

        if(intent.hasExtra(MainScreen.NEXT_SCREEN)){
            newsList = intent.getSerializableExtra(MainScreen.NEXT_SCREEN) as News
        }

        if(newsList!=null){
            tvDueDate.setText(newsList.dueDate)
            tvTitle.setText(newsList.heading)
            tvContent.setText(newsList.content)
            Picasso.get().load(newsList.imageURL).into(imageView)
        }
    }
}