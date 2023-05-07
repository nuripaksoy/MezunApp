package com.example.mezunapp

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class Profile : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEducation: EditText
    private lateinit var etJob: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var buttonSave: Button
    private lateinit var imageView: ImageView
    private lateinit var imageUri : String
    private lateinit var etCity : EditText
    private lateinit var etCountry : EditText

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        etName = findViewById(R.id.profile_name)
        etLastName = findViewById(R.id.profile_lastName)
        etJob = findViewById(R.id.profile_job)
        etEducation = findViewById(R.id.profile_education)
        etEmail = findViewById(R.id.profile_email)
        etPhoneNumber = findViewById(R.id.profile_phoneNumber)
        etStartDate = findViewById(R.id.profile_startDate)
        etEndDate = findViewById(R.id.profile_endDate)
        buttonSave = findViewById(R.id.profile_saveButton)
        imageView = findViewById(R.id.profile_image)
        etCity = findViewById(R.id.profile_city)
        etCountry = findViewById(R.id.profile_country)

        auth = Firebase.auth

        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        getUserDataFromDatabase(userUid)

        buttonSave.setOnClickListener(){
            updateUserData(userUid)
        }

        imageView.setOnClickListener(){
            uploadImage()
        }
    }

    private fun getUserDataFromDatabase(userId: String?){
        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(userId!!)

        databaseReference.get().addOnSuccessListener {
            etName.setText(it.child("firstName").value.toString())
            etLastName.setText(it.child("lastName").value.toString())
            etEmail.setText(it.child("email").value.toString())
            etStartDate.setText(it.child("startDate").value.toString())
            etEndDate.setText(it.child("endDate").value.toString())
            etPhoneNumber.setText(it.child("phoneNumber").value.toString())
            etEducation.setText(it.child("education").value.toString())
            etJob.setText(it.child("job").value.toString())
            etCountry.setText(it.child("country").value.toString())
            etCity.setText(it.child("city").value.toString())
            Picasso.get().load(it.child("imageURL").value.toString().toUri()).into(imageView);
        }
    }

    private fun updateUserData(userId: String?){

        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(userId!!)

        val uid = auth.currentUser?.uid
        val fileName = uid.toString() +".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

        val firstName = etName.text.toString()
        val lastName = etLastName.text.toString()
        val email = etEmail.text.toString()
        val startDate = etStartDate.text.toString()
        val endDate = etEndDate.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()
        val education = etEducation.text.toString()
        val job = etJob.text.toString()
        val city = etCity.text.toString()
        val country = etCountry.text.toString()

        val user = User(firstName,lastName,startDate,endDate,email,imageUri ,education, job, phoneNumber, city, country)
        databaseReference.setValue(user)
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
            imageView.setImageURI(data?.data)
            data?.data?.let { addImageToFirebaseStorage(it) }
        }
    }

    private fun addImageToFirebaseStorage(fileUri: Uri){
        val uid = auth.currentUser?.uid
        if (fileUri != null) {
            val fileName = uid.toString() +".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

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