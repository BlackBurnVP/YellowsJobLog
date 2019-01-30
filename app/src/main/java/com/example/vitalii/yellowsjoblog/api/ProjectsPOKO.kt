package com.example.vitalii.yellowsjoblog.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProjectsPOKO {

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