package com.example.mezunapp

import android.net.Uri

data class User(var firstName : String ?=null, var lastName : String ?=null, var startDate : String ?=null,
                var endDate : String ?=null, var email : String?=null, var imageURL : String ?=null,
                var education : String ?=null, var job : String ?=null, var phoneNumber: String ?=null,
                var city: String ?=null, var country: String ?=null):java.io.Serializable