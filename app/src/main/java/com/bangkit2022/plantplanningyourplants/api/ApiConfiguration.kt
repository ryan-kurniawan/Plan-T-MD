package com.bangkit2022.plantplanningyourplants.api

import com.bangkit2022.plantplanningyourplants.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfiguration {
    companion object {
        fun getApiService() : ApiService {
            val httpLoggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

            val retrofit = Retrofit.Builder().baseUrl("http://34.128.104.253:7000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}