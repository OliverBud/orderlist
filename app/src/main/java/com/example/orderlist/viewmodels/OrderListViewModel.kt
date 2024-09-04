package com.example.orderlist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orderlist.data.OrderEventsRepository
import com.example.orderlist.data.models.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderListViewModel(val repository: OrderEventsRepository) : ViewModel() {

    private val _stateFlow = MutableStateFlow<UIState>(UIState.Loading)
    val orderEventsFlow = _stateFlow.asStateFlow()

    fun getOrderEventsList() {
        viewModelScope.launch {
            repository.getOrderEvents().collect{ newState  ->
                _stateFlow.update { newState }
            }
        }
    }

}