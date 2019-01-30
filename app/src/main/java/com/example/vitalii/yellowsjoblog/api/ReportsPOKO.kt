package com.example.vitalii.yellowsjoblog.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportsPOKO {

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