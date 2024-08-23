package com.example.orderlist.networking

import com.example.orderlist.models.OrderEvent
import retrofit2.Response
import retrofit2.http.GET

interface OrderEventsService {

    @GET("order_events")
    suspend fun getOrderEvents(): Response<List<OrderEvent>>

}