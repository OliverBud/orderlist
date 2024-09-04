package com.example.orderlist.data.models

import androidx.room.Entity
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "order_events", primaryKeys = ["id", "timestamp"])
data class OrderEvent(
    val id: String,
    val state : State,
    val price: Int,
    val item : String,
    val customer: String,
    val shelf: Shelf,
    val timestamp: Long,
    val destination: String
)

enum class State {
    CREATED, COOKING, WAITING, DELIVERED, TRASHED, CANCELLED
}

enum class Shelf {
    HOT, COLD, FROZEN, OVERFLOW, NONE
}

class OrderEventTypeConverter {

    @TypeConverter
    fun orderEventToString(orderEvent: OrderEvent): String = Gson().toJson(orderEvent)

    @TypeConverter
    fun stringToOrderEvent(json: String): OrderEvent = Gson().fromJson(json, OrderEvent::class.java)
}