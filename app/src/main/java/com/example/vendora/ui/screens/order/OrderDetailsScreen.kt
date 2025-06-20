package com.example.vendora.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vendora.R
import com.example.vendora.domain.model.order.LineItem
import com.example.vendora.domain.model.order.Order
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.brandDetails.OnLoading
import com.example.vendora.utils.wrapper.Result
import com.example.vendora.utils.wrapper.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    viewModel: OrderDetailsViewModel = hiltViewModel(),
    orderId: Long = 6655953076455,
    navigateUp: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val order = viewModel.order.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.collectOrder(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = { navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "navigate back"
                        )
                    }
                },
                title = { Text("Order Information") }
            )
        }
    ) { innerPadding ->
        when (order.value) {
            is Result.Failure -> OnError { viewModel.collectOrder(orderId) }
            is Result.Loading -> OnLoading()
            is Result.Success -> {
                val parsedOrder = (order.value as Result.Success<SingleOrderResponse>).data.order
                OnSuccess(
                    paddingValues = innerPadding,
                    order = parsedOrder,
                )
            }
        }
    }

}

@Composable
fun OnSuccess(
    paddingValues: PaddingValues,
    order: Order,
) {
    val verticalScroll = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(verticalScroll)
    ) {
        ProductsList(
            items = order.line_items,
            currency = order.currency
        )
        Spacer(modifier = Modifier.height(16.dp))
        DiscountSection(order = order)
        Spacer(modifier = Modifier.height(16.dp))
        InfoSection(order = order)
    }
}

@Composable
fun ProductsList(items: List<LineItem>, currency: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items.forEach {
                ProductItem(it, currency)
            }
        }
    }
}

@Composable
fun ProductItem(item: LineItem, currency: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                painter = painterResource(R.drawable.product),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                item.name,
                maxLines = 1,

                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                "Qty = ${item.quantity}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(18.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 8.dp)
                .wrapContentWidth()
        ) {
            Text(
                item.price,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                currency,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun DiscountSection(order: Order) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DiscountLine("Amount", order.total_line_items_price,order.currency)
            DiscountLine("Discount", order.total_discounts, "- ${ order.currency }")
            DiscountLine("Discount Code", "SUMMER25")
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            DiscountLine("Total", order.total_price,order.currency)
        }
    }
}

@Composable
fun DiscountLine(title: String, amount: String, currency: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(title, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            currency,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            amount,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun InfoSection(order: Order) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DiscountLine("Payment Method", if (order.financial_status == "paid") "Visa" else "Cash on Delivery" )
            DiscountLine("Data", order.created_at.formatTimestamp() )
            DiscountLine("Confirmation number", order.confirmation_number)
        }
    }
}