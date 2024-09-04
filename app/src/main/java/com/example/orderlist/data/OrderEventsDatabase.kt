package com.example.orderlist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.orderlist.data.models.OrderEvent

@Database(entities = [OrderEvent::class], version = 1)
abstract class OrderEventsDatabase: RoomDatabase() {
    abstract fun orderEventsDao(): OrderEventsDAO
}