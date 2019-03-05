package com.example.vitalii.yellowsjoblog.api


data class Clients(var id:Int?, var name:String?, var color:String?)

data class Projects(var id:Int?, var name:String?, var client:List<Client>?, var user:List<User>){
    data class User(var id:Int, var username: String?)
    data class Client(var id:Int?, var name: String?)
}

data class Reports(var id:Int?, var name:String?, var datestamp:String?, var dateStart:String, var dateEnd:String, var user:String?, var projectColor:String?,
                   var projectName:String?, var date:String, var hourStart:String?, var hourEnd:String?)

data class Users(var id:Int?, var fullName:String?, var email:String?, var role:String?, var permission: List<Users.Permission>?, var isActive:Boolean?){
    data class Permission(var function:String?, var name:String?)
}

data class Token(val token:String, val username:String, val id:String)

data class AuthentificationData(val Username:String, val Password:String)