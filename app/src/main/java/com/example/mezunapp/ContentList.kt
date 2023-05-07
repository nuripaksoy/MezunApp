package com.example.mezunapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ContentList : AppCompatActivity() {

    private lateinit var contentList : ArrayList<Content>
    private lateinit var recyclerView : RecyclerView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        recyclerView = findViewById(R.id.content_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        contentList = arrayListOf<Content>()
        getContentList()
    }

    private fun getContentList(){
        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("Content")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (contentSnapshot in snapshot.children){
                        val content = contentSnapshot.getValue(Content::class.java)
                        contentList.add(content!!)
                    }
                    val adapter = ContentAdapter(contentList)
                    recyclerView.adapter = adapter
                    getContentDetailView(adapter)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getContentDetailView(adapter: ContentAdapter){
        adapter.setOnClickListener(object : ContentAdapter.OnClickListener{
            override fun onClick(position: Int, model: Content) {
                val intent = Intent(this@ContentList, ContentDetailView::class.java)
                intent.putExtra(ContentList.NEXT_SCREEN, model)
                startActivity(intent)
            }
        })
    }

    companion object{
        val NEXT_SCREEN="details_screen"
    }
}