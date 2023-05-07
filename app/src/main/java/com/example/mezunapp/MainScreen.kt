package com.example.mezunapp

import android.app.Notification.Action
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainScreen : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView : NavigationView

    private lateinit var newsArrayList: ArrayList<News>
    lateinit var heading : Array<String>
    lateinit var newsRecyclerView: RecyclerView
    private lateinit var databaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        //Navigation Bar
        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_account -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_alumni_list ->{
                    val intent = Intent(this, AlumniList::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_add_news-> {
                    val intent = Intent(this, AddNews::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_changePassword-> {
                    val intent = Intent(this, ChangePassword::class.java)
                        startActivity(intent)
                        true
                }
                R.id.nav_content ->{
                    val intent = Intent(this, AddContent::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_content_list -> {
                    val intent = Intent(this, ContentList::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        newsRecyclerView = findViewById(R.id.recyclerView_main)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.setHasFixedSize(true)

        newsArrayList = arrayListOf<News>()
        getNewsFromDatabase()

    }

    private fun getNewsFromDatabase() {
        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("News")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (newsSnapshot in snapshot.children){
                        val news = newsSnapshot.getValue(News::class.java)
                        newsArrayList.add(news!!)
                    }
                    val adapter = NewsAdapter(newsArrayList)
                    newsRecyclerView.adapter = adapter
                    getNewsDetailView(adapter)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun getNewsDetailView(adapter: NewsAdapter){
        adapter.setOnClickListener(object : NewsAdapter.OnClickListener{
            override fun onClick(position: Int, model: News) {
                val intent = Intent(this@MainScreen, NewsDetailView::class.java)
                intent.putExtra(NEXT_SCREEN, model)
                startActivity(intent)
            }
        })
    }

    companion object{
        val NEXT_SCREEN="details_screen"
    }

}