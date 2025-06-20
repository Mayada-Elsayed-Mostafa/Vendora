package com.example.vendora.ui.screens.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.vendora.R
import com.example.vendora.domain.model.order.Order
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.brandDetails.OnLoading
import com.example.vendora.utils.wrapper.Result
import com.example.vendora.utils.wrapper.formatTimestamp

@Composable
fun CustomerOrders(
    viewModel: OrderViewModel = hiltViewModel(),
    navigateToOrderDetails: (Long) -> Unit,
    navigateUp: () -> Unit = {}
) {
    val orders = viewModel.orders.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.collectOrders()
    }

    Scaffold(
        topBar = {
            OrdersScreenTopBar(
                navigateUp = navigateUp,
                deleteAllOrders = {}
            )
        }
    ) { innerPadding ->
        println(innerPadding)
        when (orders.value) {
            is Result.Failure -> OnError { viewModel.collectOrders() }
            Result.Loading -> OnLoading()
            is Result.Success -> {
                OnSuccess(
                    orders = (orders.value as Result.Success).data.orders,
                    paddingValues = innerPadding,
                    navigateToOrderDetails = navigateToOrderDetails
                )
            }
        }
    }
}

@Composable
fun OnSuccess(
    orders: List<Order>,
    paddingValues: PaddingValues,
    navigateToOrderDetails: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp)
    ) {
        items(
            items = orders
        ) { order ->
            OrderItem(
                order = order,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        navigateToOrderDetails(order.id)
                    }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreenTopBar(
    navigateUp: () -> Unit,
    deleteAllOrders: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        },
        actions = {
            IconButton(onClick = deleteAllOrders) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "delete all orders"
                )
            }
        },
        title = { Text("Transaction History") }
    )
}

@Composable
fun OrderItem(order: Order, modifier: Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(
                        if (order.financial_status == "paid") R.drawable.card else R.drawable.cash_on_delivery
                    )
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    order.line_items[0].title,
                    maxLines = 1    ,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    order.created_at.formatTimestamp(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    "${order.currency} ${order.total_price}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    if (order.financial_status == "paid") "Visa" else "COD",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        HorizontalDivider()
    }
}