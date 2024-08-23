package com.example.orderlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.orderlist.models.UIState
import com.example.orderlist.networking.OrderEventsApi
import com.example.orderlist.networking.OrderEventsClient
import com.example.orderlist.ui.theme.OrderListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val orderEventsRepository = OrderEventsRepository(OrderEventsApi.api)
        val viewModel by lazy { OrderListViewModel(orderEventsRepository) }
        viewModel.getOrderEventsList()

        setContent {
            OrderListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OrderEventsList(
                        viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderEventsList(
    orderEventsViewModel: OrderListViewModel,
    modifier: Modifier = Modifier
) {

    val uiState by orderEventsViewModel.orderEventsFlow.collectAsState()
    when (uiState) {
        is UIState.Loading -> CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        is UIState.Failure -> Text(
            text = "Order Events List Failure",
            modifier = modifier
        )

        is UIState.Success -> LazyColumn {
            items((uiState as UIState.Success).orderEvents) { event -> Text(text = event.customer)}
        }

    }
}
