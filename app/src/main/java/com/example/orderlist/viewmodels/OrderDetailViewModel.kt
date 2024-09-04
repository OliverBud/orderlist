package com.example.orderlist.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderlist.data.OrderEventsDatabase
import com.example.orderlist.data.models.OrderEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(val orderEventsDatabase: OrderEventsDatabase) : ViewModel() {

    private val _stateFlow = MutableStateFlow<List<OrderEvent>>(listOf())
    val orderEventsFlow = _stateFlow.asStateFlow()

    fun getOrderEventsForId(id: String) {
        viewModelScope.launch {
            Log.d("orderdetail", "get Events from database")
            val orderEventsList = orderEventsDatabase.orderEventsDao().getEventsById(id)
            Log.d("orderdetail", "database events list size: ${orderEventsList.size}" )

            _stateFlow.emit(orderEventsList)
        }
    }
}