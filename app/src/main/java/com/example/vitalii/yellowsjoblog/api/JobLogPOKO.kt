package com.example.vitalii.yellowsjoblog.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Clients {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("color")
    @Expose
    var color: String? = null
}

class Projects {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("client")
    @Expose
    var client: List<Client>? = null
    @SerializedName("users")
    @Expose
    var users: List<User>? = null

    inner class User {
        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("username")
        @Expose
        var username: String? = null
    }

    inner class Client {
        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
    }
}

class Reports {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("datestamp")
    @Expose
    var datestamp: String? = null
    @SerializedName("user")
    @Expose
    var user: String? = null
    @SerializedName("projectColor")
    @Expose
    var projectColor: String? = null
    @SerializedName("projectName")
    @Expose
    var projectName: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("hourstart")
    @Expose
    var hourstart: String? = null
    @SerializedName("hourend")
    @Expose
    var hourend: String? = null

}

class Users {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("fullName")
    @Expose
    var fullName: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("role")
    @Expose
    var role: String? = null
    @SerializedName("permissions")
    @Expose
    var permissions: List<Permission>? = null
    @SerializedName("isActive")
    @Expose
    var isActive: Boolean? = null

    inner class Permission {

        @SerializedName("function")
        @Expose
        var function: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
    }
}

data class Token(val token:String)

data class AuthentificationData(val Username:String, val Password:String)