package com.codennamdi.ancopapp.models


import com.google.gson.annotations.SerializedName

data class X1(
    @SerializedName("num")
    val num: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("verses")
    val verses: List<String>
)