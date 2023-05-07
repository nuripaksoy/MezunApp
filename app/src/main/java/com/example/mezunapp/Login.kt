package com.example.mezunapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvSignUpRedirect: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etEmail = findViewById(R.id.login_email)
        etPassword = findViewById(R.id.login_password)
        btnLogin = findViewById(R.id.login_button)
        tvSignUpRedirect = findViewById(R.id.signupRedirectText)

        auth = Firebase.auth

        tvSignUpRedirect.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener(){
            login()
        }
    }

    private fun login(){
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this,"Giriş işlemi başarılı!",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainScreen::class.java)
                startActivity(intent)
            } else{
                Toast.makeText(this,"Email veya şifre hatalı!",Toast.LENGTH_SHORT).show()
            }
        }
    }

     override fun onBackPressed() {
    }
    
}