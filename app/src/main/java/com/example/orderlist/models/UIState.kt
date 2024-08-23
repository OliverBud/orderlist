package com.example.orderlist.models

sealed interface UIState {

    object Loading : UIState

    data class Success(val orderEvents: List<OrderEvent>) : UIState

    data class Failure(val throwable: Throwable? = null) : UIState
}