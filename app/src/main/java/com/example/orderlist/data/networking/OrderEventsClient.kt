package com.example.orderlist.data.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val LOCAL_HOST_BASE_URL = "http://192.168.1.59:8080"

object OrderEventsClient {
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(LOCAL_HOST_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object OrderEventsApi {
    val api by lazy {
        OrderEventsClient.retrofit.create(OrderEventsService::class.java)
    }
}

