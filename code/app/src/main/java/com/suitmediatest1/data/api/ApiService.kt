package com.suitmediatest1.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUserDate(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Call<UserResponse>
}
