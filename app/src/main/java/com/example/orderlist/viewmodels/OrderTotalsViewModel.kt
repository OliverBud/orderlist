package com.example.orderlist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderlist.data.OrderEventsRepository
import com.example.orderlist.OrderTotalsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderTotalsViewModel(val repository: OrderEventsRepository): ViewModel() {

    private val _stateFlow = MutableStateFlow<OrderTotalsState>(OrderTotalsState(0, 0, 0, 0, 0))
    val orderTotalsFlow = _stateFlow.asStateFlow()


    fun getOrderTotals() {
        viewModelScope.launch {
            repository.getOrderTotals().collect { newState ->
                _stateFlow.update { newState }
            }
        }
    }

}