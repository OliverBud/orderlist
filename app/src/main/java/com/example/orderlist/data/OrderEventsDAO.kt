package com.example.orderlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.orderlist.data.models.OrderEvent

@Dao
interface OrderEventsDAO {

    @Insert
    suspend fun insertAll(orderEvents: List<OrderEvent>)

    @Query("SELECT * FROM order_events WHERE id is :id")
    suspend fun getEventsById(id: String): List<OrderEvent>
}