package com.example.mezunapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AlumniList : AppCompatActivity() {

    private lateinit var userList : ArrayList<User>
    private lateinit var recyclerView : RecyclerView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumni_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userList = arrayListOf<User>()
        getUserList()
    }

    private fun getUserList(){
        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        userList.add(user!!)
                    }
                    val adapter = AlumniAdapter(userList)
                    recyclerView.adapter = adapter
                    getUserDetailView(adapter)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getUserDetailView(adapter: AlumniAdapter){
        adapter.setOnClickListener(object : AlumniAdapter.OnClickListener{
            override fun onClick(position: Int, model: User) {
                val intent = Intent(this@AlumniList, AlumniDetailView::class.java)
                intent.putExtra(NEXT_SCREEN, model)
                startActivity(intent)
            }
        })
    }

    companion object{
        val NEXT_SCREEN="details_screen"
    }
}
