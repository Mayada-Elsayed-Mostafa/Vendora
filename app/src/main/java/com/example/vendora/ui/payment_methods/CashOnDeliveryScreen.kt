package com.example.vendora.ui.payment_methods

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.vendora.R
import com.example.vendora.domain.model.order.OrderPaymentResult
import com.example.vendora.domain.model.order.SingleOrderResponse
import com.example.vendora.ui.cart_screen.viewModel.CartViewModel
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.order.DiscountSection
import com.example.vendora.ui.screens.order.InfoSection
import com.example.vendora.ui.screens.order.OnLoading
import com.example.vendora.ui.screens.order.PaymentResultViewModel
import com.example.vendora.ui.screens.order.ProductsList
import com.example.vendora.utils.wrapper.Result


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashOnDeliveryScreen(
    token: String,
    orderId: Int,
    type: String,
    discountCode: String,
    onNavigateBack: () -> Unit,
    viewModel: PaymentResultViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("CashOnDelivery", type)
        viewModel.getOrderResult(orderId, token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.value.orderCreationResult is Result.Success) {
                                cartViewModel.clearCart()
                            }
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "navigate back"
                        )
                    }
                },
                title = { Text("Order Confirmation") }
            )
        }
    ) { innerPadding ->
        val context = LocalContext.current
        when (state.value.result) {
            is Result.Failure -> OnError {}
            Result.Loading -> OnLoading()
            is Result.Success -> {

                val result = (state.value.result as Result.Success).data
                LaunchedEffect(Unit) {
                    cartViewModel.checkOrCreateCart()
                    viewModel.createOrder(result, type, discountCode,context)
                }

                OnSuccess(
                    paddingValues = innerPadding,
                    result = (state.value.result as Result.Success).data,
                    creationResult = state.value.orderCreationResult,
                )
            }
        }
    }

    BackHandler {
        if (state.value.orderCreationResult is Result.Success) {
            cartViewModel.clearCart()
        }
        onNavigateBack()
    }
}

@Composable
fun OnSuccess(
    paddingValues: PaddingValues,
    result: OrderPaymentResult,
    creationResult: Result<SingleOrderResponse>,
) {

    val scrollState = rememberScrollState()

    when (creationResult) {
        is Result.Failure -> {}
        Result.Loading -> OnLoading()
        is Result.Success -> {
            val orderCreated = creationResult.data.order
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
                    .verticalScroll(scrollState)
            ) {
                DeliveryAnimation()
                Spacer(modifier = Modifier.height(16.dp))
                ProductsList(
                    items = orderCreated.line_items,
                    currency = result.currency
                )
                Spacer(modifier = Modifier.height(16.dp))
                DiscountSection(
                    order = orderCreated
                )
                Spacer(modifier = Modifier.height(16.dp))
                InfoSection(
                    order = orderCreated
                )
            }
        }
    }
}

@Composable
fun DeliveryAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.delivery_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f),
        progress = { progress },
    )
}