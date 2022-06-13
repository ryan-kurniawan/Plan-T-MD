package com.bangkit2022.plantplanningyourplants.api

import com.bangkit2022.plantplanningyourplants.response.PlantResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    companion object {
        private const val API = "Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwibmFtZSI6ImZvb2RpY3RpdmUiLCJpYXQiOjE0MjU0NzM1MzV9.-Y8OIj3dmi4Khzur8LiuHGy-3M7OSvi4ERE2QkNS4kQ"
    }

    @Headers(API)
    @GET("plants")
    fun getPlant(
        @Query("name") name : String
    ) : Call<PlantResponse>
}