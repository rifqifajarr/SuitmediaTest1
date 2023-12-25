package com.suitmediatest1.data.api

import com.google.gson.annotations.SerializedName
import com.suitmediatest1.data.model.UserModel

data class UserResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("total")
    val total: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("data")
    val data: List<UserModel>,
)
