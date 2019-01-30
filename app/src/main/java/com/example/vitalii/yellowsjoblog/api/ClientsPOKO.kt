package com.example.vitalii.yellowsjoblog.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClientsPOKO {
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
