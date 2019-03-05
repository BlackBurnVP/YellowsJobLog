package com.example.vitalii.yellowsjoblog.api

import java.util.*

data class AddClient (val name:String, val color:String )

data class AddProject(val name:String, val client:String, val users:ArrayList<String>)

data class EndTask(val name:String, val datestamp_end:String, val project: String, val user:String)

data class AddTask(val name:String, val project:String, val user:String)

data class UpdateTask(val name:String, val project:String, val datestamp:String, val datestamp_end: String)