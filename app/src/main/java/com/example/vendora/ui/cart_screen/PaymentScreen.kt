package com.example.vendora.ui.cart_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.domain.model.payment.BillingData
import com.example.vendora.domain.model.payment.OrderRequest
import com.example.vendora.domain.model.payment.OrderResponse
import com.example.vendora.domain.model.payment.PaymentKeyRequest
import com.example.vendora.domain.model.payment.PaymentKeyResponse
import com.example.vendora.ui.cart_screen.viewModel.PaymobViewModel
import com.example.vendora.ui.screens.address.viewModel.AddressViewModel
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.StateFlow


@Composable
fun PaymentScreen(token:String, totalPrice : Double, orderId: Int, addressViewModel: AddressViewModel = hiltViewModel(), viewModel: PaymobViewModel= hiltViewModel(), navController: NavHostController) {

    val defaultAddress by addressViewModel.defaultAddress.collectAsState()
    var billing_data = BillingData()
    if (defaultAddress != null){
       println(defaultAddress)
        billing_data = BillingData(
            city = defaultAddress!!.city,
            country = defaultAddress!!.country,
            phone_number = defaultAddress!!.phone ,
            state = defaultAddress!!.type ,
            apartment = defaultAddress!!.address,
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getPaymentKey(paymentKeyRequest = PaymentKeyRequest(
            auth_token = token,
            amount_cents = totalPrice.toInt(),
            order_id = orderId,
            billing_data =  billing_data,
            integration_id = 5134520,
        ))
    }

    val paymentKeyState by viewModel.paymentKeyState.collectAsState()


    Column (
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp),
    ) {
        CustomAppBar("Checkout Screen : $totalPrice EGP"){navController.popBackStack()}


        PaymentMethodItem ("Visa", navToPaymentMethod = {
            if (paymentKeyState is Result.Success){
                val paymentToken = (paymentKeyState as Result.Success<PaymentKeyResponse>).data.token
                println("final token : $paymentToken")

                navController.navigate(ScreenRoute.VisaScreenRoute(
                    token = paymentToken,
                    orderId = orderId,
                    firstToken = token
                ))
            }
        })
        Divider()
        PaymentMethodItem ("PayPal", navToPaymentMethod = {/**/})
        Divider()
        PaymentMethodItem ("Vodafone Cash", navToPaymentMethod = {/**/})

    }
}


@Composable
fun PaymentMethodItem (title:String ,navToPaymentMethod:()->Unit ) {

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
            Row (
                modifier =  Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row (
                    modifier =  Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.onSurface),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "adddress",
                        tint = MaterialTheme.colorScheme.surfaceContainer,
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