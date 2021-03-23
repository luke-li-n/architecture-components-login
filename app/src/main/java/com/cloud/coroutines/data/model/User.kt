package com.cloud.coroutines.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("email")
    var email: String = "",
    @SerializedName("nickname")
    var nickname: String = "",
    @SerializedName("username")
    var username: String = ""
)