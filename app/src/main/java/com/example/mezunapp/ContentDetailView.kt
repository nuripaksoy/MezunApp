package com.example.mezunapp

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class ContentDetailView : AppCompatActivity() {
    private lateinit var imageView : ImageView
    private lateinit var contentText : TextView
    private lateinit var contentTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_detail_view)

        imageView = findViewById(R.id.contentDTO_image)
        contentText = findViewById(R.id.contentDTO_text)
        contentTitle = findViewById(R.id.contentDTO_title)

        var contentList : Content?=null

        if(intent.hasExtra(MainScreen.NEXT_SCREEN)){
            contentList = intent.getSerializableExtra(MainScreen.NEXT_SCREEN) as Content
        }

        if(contentList!=null){
            contentTitle.setText(contentList.header)
            contentText.setText(contentList.text)
            Picasso.get().load(contentList.image).into(imageView)
        }

    }
}