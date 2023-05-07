package com.example.mezunapp

data class News(var heading: String ?= null, var publishDate: String ?=null, var dueDate: String ?=null,
                var content: String ?=null , var ownerFullName: String ?=null, var imageURL: String ?=null):java.io.Serializable
