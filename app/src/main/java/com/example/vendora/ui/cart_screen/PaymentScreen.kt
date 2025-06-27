package com.example.vendora.ui.cart_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.vendora.R
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.domain.model.payment.BillingData
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.ui.cart_screen.viewModel.PaymobViewModel
import com.example.vendora.ui.screens.address.viewModel.AddressViewModel
import com.example.vendora.utils.wrapper.Result


@Composable
fun PaymentScreen(
    token: String,
    totalPrice: Double,
    orderId: Int,
    discountCode: String,
    addressViewModel: AddressViewModel = hiltViewModel(),
    viewModel: PaymobViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val defaultAddress by addressViewModel.defaultAddress.collectAsState()
    var billing_data = BillingData()
    if (defaultAddress != null) {
        println(defaultAddress)
        billing_data = BillingData(
            city = defaultAddress!!.city,
            country = defaultAddress!!.country,
            phone_number = defaultAddress!!.phone,
            state = defaultAddress!!.type,
            apartment = defaultAddress!!.address,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getPaymentKey(
            paymentKeyRequest = PaymentKeyRequest(
                auth_token = token,
                amount_cents = totalPrice.toInt(),
                order_id = orderId,
                billing_data = billing_data,
                integration_id = 5134520,
            )
        )
    }

    val paymentKeyState by viewModel.paymentKeyState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp),
    ) {
        CustomAppBar("Payment Methods") { navController.popBackStack() }

        CustomLottie()

        PaymentMethodItem("Online Card", R.drawable.visa, navToPaymentMethod = {
            if (paymentKeyState is Result.Success) {
                val paymentToken =
                    (paymentKeyState as Result.Success<PaymentKeyResponse>).data.token
                println("final token : $paymentToken")

                navController.navigate(
                    ScreenRoute.VisaScreenRoute(
                        token = paymentToken,
                        orderId = orderId,
                        firstToken = token,
                        discountCode = discountCode
                    )
                )
            }
        })
        /*        Divider()
                PaymentMethodItem ("Wallet", R.drawable.wallet,navToPaymentMethod = {*//**//*})*/

        Divider()
        PaymentMethodItem("Cash on delivery", R.drawable.cash_on_delivery, navToPaymentMethod = {
            navController.navigate(
                ScreenRoute.CashDeliveryScreen(
                    firstToken = token,
                    orderId = orderId,
                    type = "Cash",
                    discountCode = discountCode
                )
            )
        })

    }
}


@Composable
fun PaymentMethodItem(title: String, icon: Int, navToPaymentMethod: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { navToPaymentMethod() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Row(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.onSurface),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(

                        painter = painterResource(icon),
                        contentDescription = "adddress",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                }

            }



            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "payment",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        /*nav to payment method*/
                        navToPaymentMethod()
                    }
            )


        }
    }
}


@Composable
fun CustomLottie() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.visa))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            progress = { progress },
        )

    }

}