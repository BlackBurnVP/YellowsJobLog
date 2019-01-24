package com.example.vitalii.yellowsjoblog.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobLogPOJO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("datestamp")
    @Expose
    private String datestamp;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("projectColor")
    @Expose
    private String projectColor;
    @SerializedName("projectName")
    @Expose
    private String projectName;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("hourstart")
    @Expose
    private String hourstart;
    @SerializedName("hourend")
    @Expose
    private String hourend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(String projectColor) {
        this.projectColor = projectColor;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHourstart() {
        return hourstart;
    }

    public void setHourstart(String hourstart) {
        this.hourstart = hourstart;
    }

    public String getHourend() {
        return hourend;
    }

    public void setHourend(String hourend) {
        this.hourend = hourend;
    }
}