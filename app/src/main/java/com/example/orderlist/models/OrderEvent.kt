package com.example.orderlist.models

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