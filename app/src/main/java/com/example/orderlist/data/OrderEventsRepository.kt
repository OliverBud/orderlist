package com.example.orderlist.data

import android.util.Log
import com.example.orderlist.OrderTotals
import com.example.orderlist.OrderTotalsState
import com.example.orderlist.data.models.OrderEvent
import com.example.orderlist.data.models.State
import com.example.orderlist.data.models.UIState
import com.example.orderlist.data.networking.OrderEventsService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OrderEventsRepository(
    val api: OrderEventsService,
    val orderTotals: OrderTotals,
    val orderEventsDatabase: OrderEventsDatabase
) {
    var ordersFlow: MutableSharedFlow<List<OrderEvent>> = MutableSharedFlow()
    var apiErrorFlow: MutableSharedFlow<Throwable> = MutableSharedFlow()

    suspend fun startOrderEventsFlow() {
        while (true) {
            val response = api.getOrderEvents()
            when (response.isSuccessful) {
                true ->
                    ordersFlow.emit(response.body() ?: listOf())

                false ->
                    apiErrorFlow.emit(Throwable(response.errorBody()?.string() ?: "Api Failure"))
            }
            delay(2000)
        }
    }

    suspend fun connectOrdersDao() {
        ordersFlow.collect {
            Log.d("orderdetail", "saving Orders")
            orderEventsDatabase.orderEventsDao().insertAll(it)
        }
    }

    fun getOrderEvents(): Flow<UIState> = ordersFlow.map { UIState.Success(it) }

    suspend fun getOrderTotals(): Flow<OrderTotalsState> = flow {
            ordersFlow.collect { orderEvents ->
                for (event in orderEvents) {
                    when(event.state) {
                        State.DELIVERED -> {
                            orderTotals.orderFinished(event)
                            emit(orderTotals.getOrderTotalsState())
                        }
                        State.TRASHED -> {
                            orderTotals.orderFinished(event)
                            emit(orderTotals.getOrderTotalsState())
                        }
                        else -> {}
                    }
                }
            }
        }
}
