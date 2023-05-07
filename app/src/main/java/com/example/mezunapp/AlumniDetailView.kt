package com.example.mezunapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso

class AlumniDetailView : AppCompatActivity() {

    private lateinit var tvEmail: TextView
    private lateinit var tvfullName : TextView
    private lateinit var tvJob : TextView
    private lateinit var tvEducation : TextView
    private lateinit var tvStartDate : TextView
    private lateinit var tvEndDate : TextView
    private lateinit var tvPhoneNumber : TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumni_detail_view)

        tvEmail = findViewById(R.id.alumniDTO_email)
        tvEducation = findViewById(R.id.alumniDTO_education)
        tvJob = findViewById(R.id.alumniDTO_job)
        tvStartDate = findViewById(R.id.alumniDTO_startDate)
        tvEndDate = findViewById(R.id.alumniDTO_endDate)
        tvfullName = findViewById(R.id.alumniDTO_fullName)
        tvPhoneNumber = findViewById(R.id.alumniDTO_phoneNumber)
        imageView = findViewById(R.id.alumniDTO_image)

        var userList : User?=null

        if(intent.hasExtra(AlumniList.NEXT_SCREEN)){
            userList = intent.getSerializableExtra(AlumniList.NEXT_SCREEN) as User
        }

        if(userList!=null){
            tvEmail.setText(userList.email)
            tvfullName.setText(userList.firstName +" "+userList.lastName)
            tvEducation.setText(userList.education)
            tvJob.setText(userList.job)
            tvStartDate.setText(userList.startDate)
            tvEndDate.setText(userList.endDate)
            tvPhoneNumber.setText(userList.phoneNumber)
            Picasso.get().load(userList.imageURL).into(imageView);
        }

        tvEmail.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse( "mailto:"+ tvEmail.text.toString())
                putExtra(Intent.EXTRA_SUBJECT, "Konu")
                putExtra(Intent.EXTRA_TEXT, "İçerik")
            }
            startActivity(intent)
        }

        tvPhoneNumber.setOnClickListener(){
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${tvPhoneNumber.text.toString()}")
            }
            startActivity(intent)
        }
    }
}