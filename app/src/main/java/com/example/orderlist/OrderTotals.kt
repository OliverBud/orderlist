package com.example.orderlist

import android.util.Log
import com.example.orderlist.data.models.OrderEvent
import com.example.orderlist.data.models.State

class OrderTotals {

    var trashed = 0
    var delivered = 0
    var totalSales = 0
    var totalWasted = 0
    var totalRevenue = 0

    fun orderFinished(order: OrderEvent) {
        Log.d("orderTotals", "update for order Finished")

        when(order.state) {
            State.DELIVERED -> {
                delivered++
                totalSales += order.price
                totalRevenue = totalSales - totalWasted
            }
            State.TRASHED -> {
                trashed++
                totalWasted += order.price
                totalRevenue = totalSales - totalWasted
            }
            else -> {}
        }
    }

    fun getOrderTotalsState(): OrderTotalsState {
        return OrderTotalsState(trashed, delivered, totalSales, totalWasted, totalRevenue)
    }
}

data class OrderTotalsState(
    val trashed: Int,
    val delivered: Int,
    val totalSales: Int,
    val totalWasted: Int,
    val totalRevenue: Int
)