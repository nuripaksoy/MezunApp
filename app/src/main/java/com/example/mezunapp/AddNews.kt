package com.example.mezunapp

import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNews : AppCompatActivity() {

    private lateinit var etTitle : EditText
    private lateinit var etContent : EditText
    private lateinit var etDueDate : EditText
    private lateinit var titleImage : ImageView
    private lateinit var addNewsButton : Button
    private lateinit var imageUri : String

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_news)

        auth = Firebase.auth

        etTitle = findViewById(R.id.news_title)
        etContent = findViewById(R.id.news_content)
        etDueDate = findViewById(R.id.news_dueDate)
        titleImage = findViewById(R.id.news_image)
        addNewsButton = findViewById(R.id.news_button)

        addNewsButton.setOnClickListener(){
            addNews()
        }

        titleImage.setOnClickListener(){
            uploadImage()
        }
    }

    private fun addNews(){
        val uid = auth.currentUser?.uid
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentDate = LocalDateTime.now().format(formatter)
        val content = etContent.text.toString()
        val dueDate = etDueDate.text.toString()
        val title = etTitle.text.toString()
        val publishDate = currentDate.toString()
        val owner = getUserNameFromDatabase(uid)


        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("News")
        val news = News(title, publishDate,dueDate, content, owner, imageUri)

        if(uid!=null){
            databaseReference.child(uid + " (" +currentDate +")").setValue(news)
        }
    }

    private fun getUserNameFromDatabase(userId: String?): String {
        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(userId!!)
        var fullName : String
        fullName = ""
        databaseReference.get().addOnSuccessListener {
            fullName = it.child("firstName").value.toString() + " " + it.child("lastName").value.toString()
        }
        return fullName
    }

    private fun uploadImage(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            titleImage.setImageURI(data?.data)
            data?.data?.let { addImageToFirebaseStorage(it) }
        }
    }

    private fun addImageToFirebaseStorage(fileUri: Uri){
        val uid = auth.currentUser?.uid
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentDate = LocalDateTime.now().format(formatter)
        if (fileUri != null) {
            val fileName = currentDate +".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("images/news/$uid/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUri = it.toString()
                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }
}
