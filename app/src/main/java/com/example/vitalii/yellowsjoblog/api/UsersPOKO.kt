package com.example.vitalii.yellowsjoblog.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
