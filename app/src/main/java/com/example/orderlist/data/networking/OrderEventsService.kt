package com.example.orderlist.data.networking

import com.example.orderlist.data.models.OrderEvent
import retrofit2.Response
import retrofit2.http.GET

interface OrderEventsService {

    @GET("order_events")
    suspend fun getOrderEvents(): Response<List<OrderEvent>>

}