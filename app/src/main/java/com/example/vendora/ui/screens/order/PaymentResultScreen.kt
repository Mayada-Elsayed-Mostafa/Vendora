package com.example.vendora.ui.screens.order

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.vendora.utils.wrapper.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentResultScreen(
    viewModel: PaymentResultViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    orderId: Int,
    token: String,
    isSuccess: Boolean,
    discountCode: String
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
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
                title = { Text("Transaction Result") }
            )
        }
    ) { innerPadding ->

        val context = LocalContext.current

        when (state.value.result) {
            is Result.Failure -> OnError {}
            Result.Loading -> OnLoading()
            is Result.Success -> {
//                Log.d("Payment","PaymentStatus = ${(state.value.result as Result.Success).data.payment_status}")
//                Log.d("Payment","PaymentStatus = ${(state.value.result as Result.Success).data}")
                val result = (state.value.result as Result.Success).data
                LaunchedEffect(Unit) {
                    cartViewModel.checkOrCreateCart()
                    if (isSuccess)
                        viewModel.createOrder(result, discountCode = discountCode, context = context)
                }

                OnSuccess(
                    paddingValues = innerPadding,
                    result = (state.value.result as Result.Success).data,
                    creationResult = state.value.orderCreationResult,
                    onNavigateBack = onNavigateBack,
                    isSuccess = isSuccess,
                    cartViewModel = cartViewModel
                )
            }
        }
    }

    BackHandler {
        onNavigateBack()
    }

}

@Composable
fun OnLoading() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )

        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .fillMaxSize(),
            progress = { progress },
        )
    }
}

@Composable
fun OnSuccess(
    paddingValues: PaddingValues,
    result: OrderPaymentResult,
    isSuccess: Boolean,
    creationResult: Result<SingleOrderResponse>,
    onNavigateBack: () -> Unit,
    cartViewModel: CartViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        val animation =
            if (isSuccess) R.raw.payment_success else R.raw.payment_failed
        val message =
            if (isSuccess) "payment successful" else "payment failed"

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )

        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .size(240.dp),
            progress = { progress },
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                cartViewModel.clearCart()
                onNavigateBack()
            },
            enabled = creationResult !is Result.Loading
        ) {
            if (creationResult is Result.Loading) {
                CircularProgressIndicator()
            } else {
                Text("Return to home")
            }
        }
    }
}