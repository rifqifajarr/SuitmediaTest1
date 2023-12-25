package com.suitmediatest1.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("first_name")
    val firstName: String = "",

    @SerializedName("last_name")
    val lastName: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("avatar")
    val avatar: String = "",
)
