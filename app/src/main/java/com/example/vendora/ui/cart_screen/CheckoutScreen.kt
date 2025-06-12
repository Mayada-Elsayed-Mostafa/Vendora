package com.example.vendora.ui.cart_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.ui.cart_screen.viewModel.PaymobViewModel
import com.example.vendora.ui.screens.address.viewModel.AddressViewModel
import com.example.vendora.utils.wrapper.Result


@Composable
fun CheckoutScreen(token:String,navController: NavHostController,viewModel: PaymobViewModel= hiltViewModel(),addressViewModel: AddressViewModel = hiltViewModel()) {
    var cartItems by  remember {
        mutableStateOf(listOf(
            CartItem(1, "Essence Mascara Lash Princess", 250.00,"https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/thumbnail.webp", "Color", "M"),
            CartItem(2, "Eyeshadow Palette with Mirror", 120.00,"https://cdn.dummyjson.com/product-images/beauty/eyeshadow-palette-with-mirror/thumbnail.webp", "Color", "M"),
            CartItem(3, "Powder Canister", 150.00,"https://cdn.dummyjson.com/product-images/beauty/powder-canister/thumbnail.webp", "Color", "M"),
            CartItem(4, "Red Lipstick", 100.00,"https://cdn.dummyjson.com/product-images/beauty/red-lipstick/thumbnail.webp", "Color", "M"),
        ))
    }

    LaunchedEffect(Unit) {
        addressViewModel.getAllAddresses()
    }

    val defaultAddress by addressViewModel.defaultAddress.collectAsState()



    var promoCode by remember { mutableStateOf("") }
    val totalPrice = cartItems.sumOf { it.price }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ){

        CustomAppBar("Checkout Screen",){navController.popBackStack()}
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        ///Shipping Address
        Text(
            text = "Shipping Address",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (defaultAddress != null){

            ShippingAddressItem(defaultAddress!!){
                navController.navigate(ScreenRoute.AddressScreen)
            }
        }else {
            Text(text = "No default address found.")
            PaymentBottom("Add New Address"){
                navController.navigate(ScreenRoute.AddressScreen)
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        ////Order List
        Text(
            text = "Order List",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cartItems){ item ->
                CheckoutItem(item)
            }

            item{
                PromoCodeItem(
                    promoCode,
                    onPromoCodeChange = { promoCode = it },
                    totalPrice, navToDiscount = {navController.navigate(ScreenRoute.DiscountScreen)})
            }
        }

        //////

        PaymentBottom("Continue to Payment"){
            /*nav to Payment Screen*/
            println("Nav To")
            navController.navigate(ScreenRoute.PaymentScreenRoute(totalPrice,token))

        }


    }
}


@Composable
fun CheckoutItem (item: CartItem  ) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.imageUrl)
                    .build(),
                contentDescription = item.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            )


            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(2f)
            )
            {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))
                // color and size
                Row( verticalAlignment = Alignment.CenterVertically)
                {
                    Text(
                        text ="${item.color}",
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Text(
                        text = " | Size = ${item.size}",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${item.price}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )

            }
            Row (
               modifier =  Modifier
                   .size(50.dp)
                   .clip(RoundedCornerShape(25.dp))
                   .background(MaterialTheme.colorScheme.surfaceContainer),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                )
            }


        }
    }
}

@Composable
fun ShippingAddressItem (item:  AddressEntity , navToAddress : ()->Unit ) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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



            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(2f)
            )
            {
                Text(
                    text = item.type,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.address,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )


            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "adddress",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navToAddress() }
            )


        }
    }
}

@Composable
fun CustomAppBar( title: String, back:()-> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(28.dp)
                .clickable { back() }
        )
        Spacer(Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )




    }
}


@Composable
fun PaymentBottom(title:String,navTo:()->Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground
            ),
            onClick = {
            /*nav to payment*/
            navTo()
            }
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Checkout",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
}
}

@Composable
fun PromoCodeItem(
    promoCode: String,
    onPromoCodeChange: (String) -> Unit,
    totalPrice: Double,
    navToDiscount:() -> Unit
) {
    var discountApplied by remember { mutableStateOf(false) }
    var finalPrice by remember { mutableStateOf(totalPrice) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val validPromoCode = "mka10"
    val discountPercentage = 10

    Column {
        Text(
            text = "Promo Code",
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground) ,
                onClick = {navToDiscount()}
            ){
                Text("Choose Discount Code")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicTextField(
                    value = promoCode,
                    onValueChange = {
                        onPromoCodeChange(it)
                        discountApplied = false
                        errorMessage = null
                        finalPrice = totalPrice
                    },
                    singleLine = true,

                    textStyle = TextStyle(fontSize = 16.sp),

                    decorationBox = { innerTextField ->
                        if (promoCode.isEmpty()) {
                            Text(
                                text = "Enter promo code",

                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    },

                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(onClick = {
                    if (promoCode.equals(validPromoCode, ignoreCase = true)) {
                        finalPrice = totalPrice * (1 - discountPercentage / 100.0)
                        discountApplied = true
                        errorMessage = null
                    } else {
                        errorMessage = "Invalid promo code"
                        discountApplied = false
                        finalPrice = totalPrice
                    }
                },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground) ,

                    ) {
                    Text("Apply")
                }
            }

            Column (Modifier.padding(horizontal = 16.dp, vertical = 8.dp)){
                errorMessage?.let {
                    Text(text = it, color = Color.Red, fontSize = 14.sp)
                }
                Divider()

                if (discountApplied) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Discount applied : ${finalPrice} EGP",
                        style = MaterialTheme.typography.titleMedium,
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total: $totalPrice} EGP",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }


    }
}

