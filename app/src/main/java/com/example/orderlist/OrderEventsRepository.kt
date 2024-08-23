package com.example.orderlist

import com.example.orderlist.models.UIState
import com.example.orderlist.networking.OrderEventsService

class OrderEventsRepository(val api: OrderEventsService) {

    suspend fun getOrderEvents(): UIState {
        val response = api.getOrderEvents()
        return when (response.isSuccessful) {
            true -> UIState.Success(response.body() ?: listOf())
            false -> UIState.Failure(Throwable(response.errorBody()?.string() ?: "Api Failure"))
        }
    }
}