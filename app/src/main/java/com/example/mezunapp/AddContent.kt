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

class AddContent : AppCompatActivity() {

    private lateinit var etTitle : EditText
    private lateinit var etText : EditText
    private lateinit var buttonPost : Button
    private lateinit var image : ImageView
    private lateinit var imageUri : String


    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_content)

        etTitle = findViewById(R.id.content_title)
        etText = findViewById(R.id.content_text)
        buttonPost = findViewById(R.id.content_post)
        image = findViewById(R.id.content_image)

        auth = Firebase.auth

        buttonPost.setOnClickListener(){
            post()
        }
        image.setOnClickListener(){
            uploadImage()
        }
    }

    private fun post(){
        val uid = auth.currentUser?.uid
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentDate = LocalDateTime.now().format(formatter)
        val title = etTitle.text.toString()
        val etText = etText.text.toString()


        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("Content")
        val content = Content(etText, uid, title, imageUri)
        if(uid!=null){
            databaseReference.child(uid + " (" +currentDate +")").setValue(content)
        }
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
            image.setImageURI(data?.data)
            data?.data?.let { addImageToFirebaseStorage(it) }
        }
    }

    private fun addImageToFirebaseStorage(fileUri: Uri){
        val uid = auth.currentUser?.uid
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentDate = LocalDateTime.now().format(formatter)
        if (fileUri != null) {
            val fileName = currentDate +".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("images/content/$uid/$fileName")

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