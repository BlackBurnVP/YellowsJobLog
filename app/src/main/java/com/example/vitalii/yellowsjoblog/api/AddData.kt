package com.example.vitalii.yellowsjoblog.api

data class AddClient (val name:String, val color:String )

data class AddProject(val name:String, val client:String, val users:ArrayList<String>)