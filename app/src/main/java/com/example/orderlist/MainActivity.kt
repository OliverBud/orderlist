package com.example.orderlist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.orderlist.data.OrderEventsDatabase
import com.example.orderlist.data.OrderEventsRepository
import com.example.orderlist.data.models.OrderEvent
import com.example.orderlist.data.models.UIState
import com.example.orderlist.data.networking.OrderEventsApi
import com.example.orderlist.ui.theme.OrderListTheme
import com.example.orderlist.viewmodels.OrderDetailViewModel
import com.example.orderlist.viewmodels.OrderListViewModel
import com.example.orderlist.viewmodels.OrderTotalsViewModel
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val orderEventsDatabase = Room.databaseBuilder(
            this, OrderEventsDatabase::class.java,
            "OrderEventDatabase")
            .build()

        val orderTotals = OrderTotals()
        val orderEventsRepository = OrderEventsRepository(OrderEventsApi.api, orderTotals, orderEventsDatabase)

        lifecycleScope.launch {
            orderEventsRepository.connectOrdersDao()
        }
        lifecycleScope.launch {
            orderEventsRepository.startOrderEventsFlow()
        }

        val orderEventsListViewModel by lazy { OrderListViewModel(orderEventsRepository) }
        orderEventsListViewModel.getOrderEventsList()

        val orderTotalsViewModel by lazy { OrderTotalsViewModel(orderEventsRepository) }
        orderTotalsViewModel.getOrderTotals()

        val orderDetailViewModel by lazy { OrderDetailViewModel(orderEventsDatabase) }

        setContent {
            OrderListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "list"){
                        composable(route = "list") {
                            Column {
                                OrderTotals(
                                    orderTotalsViewModel,
                                    modifier = Modifier
                                        .padding(innerPadding)
                                )
                                OrderEventsList(
                                    orderEventsListViewModel,
                                    navController,
                                    modifier = Modifier.padding(innerPadding).wrapContentHeight()
                                )
                            }
                        }
                        composable(
                            route = "detail/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType } )
                            ) {backstackEntry ->
                                OrderDetail(id = backstackEntry.arguments?.getString("id"), orderDetailViewModel = orderDetailViewModel)
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderTotals(
    orderTotalsViewModel: OrderTotalsViewModel,
    modifier: Modifier
) {

    val orderTotalsState by orderTotalsViewModel.orderTotalsFlow.collectAsState()

    Column(modifier
        .padding(40.dp, 40.dp, 40.dp, 10.dp)
        .fillMaxWidth()
    ) {
        Text(text = "Total Delivered: ${orderTotalsState.delivered}")
        Text(text = "Total Trashed: ${orderTotalsState.trashed}")
        Text(text = "Total Sales: ${orderTotalsState.totalSales}")
        Text(text = "Total Wasted: ${orderTotalsState.totalWasted}")
        Text(text = "Total Revenue: ${orderTotalsState.totalRevenue}")
    }

}

@Composable
fun OrderDetail(id: String?, orderDetailViewModel: OrderDetailViewModel) {

    Log.d("orderdetail", "detail id: $id")
    orderDetailViewModel.getOrderEventsForId(id!!)
    val orderEventsList by orderDetailViewModel.orderEventsFlow.collectAsState()
    LazyColumn(
        Modifier.padding(40.dp, 40.dp, 40.dp, 10.dp),
        ) {
        items(orderEventsList) {
            event -> OrderEventBox(Modifier.wrapContentHeight(), event)
        }
    }
}

@Composable
fun OrderEventsList(
    orderEventsViewModel: OrderListViewModel,
    navController: NavController,
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

        is UIState.Success -> {
            LazyColumn(
                modifier.padding(40.dp, 10.dp, 40.dp, 10.dp),
            ) {
                items((uiState as UIState.Success).orderEvents) { event ->
                    OrderEventBox(
                        Modifier.clickable { navController.navigate("detail/${event.id}") },
                        event
                    )
                }
            }
        }

    }
}


@Composable
fun OrderEventBox(modifier: Modifier, orderEvent: OrderEvent) {
    Box(modifier
        .border(2.dp, Color.Black)
        .padding(4.dp)
    ) {
        Column {
            Text(text = "timestamp: ${Date(orderEvent.timestamp)}")
            Text(text = "customer: ${orderEvent.customer}")
            Text(text = "item: ${orderEvent.item}")
            Text(text = "destination: ${orderEvent.destination}")
            Text(text = "shelf: ${orderEvent.shelf}")
            Text(text = "state: ${orderEvent.state}")
        }
    }
}
