package com.example.mezunapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File

class Register : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var btnRegister: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.register_name)
        etSurname = findViewById(R.id.register_surname)
        etEmail = findViewById(R.id.register_email)
        etPassword = findViewById(R.id.register_password)
        etStartDate = findViewById(R.id.register_startDate)
        etEndDate = findViewById(R.id.register_endDate)
        btnRegister = findViewById(R.id.register_button)

        auth = Firebase.auth

        btnRegister.setOnClickListener(){
            register()
        }
    }

    private fun register(){
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            if(it.isSuccessful){
                saveUser()
                Toast.makeText(this,"Kayıt olma işlemi başarılı!",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,Login::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Kayıt olma işlemi başarısız oldu.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUser(){
        val uid = auth.currentUser?.uid
        val firstName = etName.text.toString()
        val lastName = etSurname.text.toString()
        val email = etEmail.text.toString()
        val startDate = etStartDate.text.toString()
        val endDate = etEndDate.text.toString()

        databaseReference = FirebaseDatabase.getInstance("https://mezunapp-56b37-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

        val user = User(firstName,lastName,startDate,endDate,email)

        if(uid!=null){
            databaseReference.child(uid).setValue(user)
        }
    }
}