package com.example.vendora.ui.cart_screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.vendora.CartCreateMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.R
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.domain.model.currency.CurrencyInfo
import com.example.vendora.domain.model.currency.CurrencyResponse
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.type.Cart
import com.example.vendora.ui.cart_screen.viewModel.CartViewModel
import com.example.vendora.ui.cart_screen.viewModel.PaymobViewModel
import com.example.vendora.ui.screens.address.view.ConfirmDeleteDialog
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.brandDetails.OnLoading
import com.example.vendora.ui.screens.currency.CurrencyDropDown
import com.example.vendora.ui.screens.currency.CurrencyViewModel
import com.example.vendora.ui.screens.currency.convertToCurrency
import com.example.vendora.utils.wrapper.Result
import kotlinx.serialization.Serializable


@Composable
fun CartScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavHostController,
    viewModel: PaymobViewModel = hiltViewModel(),
    currencyViewModel: CurrencyViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState by cartViewModel.uiState.collectAsState()
    val getFirstToken by viewModel.getTokenState.collectAsState()

    val getChangeRate by currencyViewModel.getChangeRate.collectAsState()

    var firstToken by remember { mutableStateOf("") }
    val itemIdToDelete = remember { mutableStateOf<String>("") }

    LaunchedEffect(Unit) {
        viewModel.getTokenForAuthentication()
        currencyViewModel.getCurrency()
        currencyViewModel.getRates()
        // cartViewModel.loadCart(uiState.cartId?:"")
        cartViewModel.checkOrCreateCart()
    }

    val currency by currencyViewModel.selectedCurrency.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        CustomAppBar("Cart") { navController.popBackStack() }
        Spacer(modifier = Modifier.height(16.dp))

        when (val result = uiState.loadCartResult) {
            is Result.Failure -> {
                println("error $result")
                OnError { }
            }

            Result.Loading -> {
                if (uiState.isLoading ){
                    CustomLoading()
                }
            }

            is Result.Success -> {
                if (result.data.lines.edges.isEmpty()) {
                    CustomEmpty()
                } else
                {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(result.data.lines.edges) { item ->

                            CartItem(
                                item = item,
                                currency=currency,
                                onCountChange = { newQuantity ->
                                    cartViewModel.updateCartLineQuantity(lineId = item.node.id, quantity = newQuantity)
                                },
                                onDelete = {
                                    itemIdToDelete.value = item.node.id
                                },
                                getChangeRate = getChangeRate
                            )
                        }
                    }
                    when (getFirstToken) {
                        is Result.Success -> {
                            firstToken = (getFirstToken as Result.Success<AuthTokenResponse>).data.token
                            CheckoutButton(
                                totalPrice = uiState.totalAmount,
                                getChangeRate = getChangeRate,
                                currency = currency,
                                navToCheckout = {
                                    navController.navigate(ScreenRoute.CheckoutScreenRoute(firstToken))
                                }
                            )
                        }

                        is Result.Failure -> Text("Auth failed")
                        Result.Loading -> {}
                    }
                }
            }
        }


        if (itemIdToDelete.value.isNotBlank() && itemIdToDelete.value.isNotEmpty() ) {
            ConfirmDeleteDialog(
                message = "Are you sure you want to delete this product ?",
                onConfirm = {
                    cartViewModel.removeFromCart(itemIdToDelete.value)
                    itemIdToDelete.value = ""
                },
                onDismiss = {
                    itemIdToDelete.value = ""
                }
            )
        }
    }
}


@Composable
fun CartItem (item: GetCartQuery.Edge, currency:String = "EGP", onCountChange : (Int)->Unit, onDelete : ()->Unit, getChangeRate: Double) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(item.node.quantity) }
    val availableQuantity = item.node.merchandise.onProductVariant?.quantityAvailable ?: 10
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()

                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        )
        {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.node.merchandise.onProductVariant?.product?.images?.edges?.get(0)?.node?.url)
                    .crossfade(true)
                    .build(),
                contentDescription = item.node.merchandise.onProductVariant?.product?.title ?:"title",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .weight(1f)
                    .background(Color(0xFF35383f)),
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )
            {
                Text(
                    text = item.node.merchandise.onProductVariant?.product?.title ?:"title",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(4.dp))
                // color and size
                Row( verticalAlignment = Alignment.CenterVertically)
                {
                    Text(
                        text ="${item.node.merchandise.onProductVariant?.title}",
                        style = MaterialTheme.typography.titleSmall,
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${item.node.merchandise.onProductVariant?.price?.amount.toString().toDoubleOrNull()?.convertToCurrency(getChangeRate)} " + currency,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )

            }

            //count and delete
            Column (
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ){
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.trash),
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row (
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    IconButton(
                        onClick = {
                            if (quantity > 1) {
                                quantity--
                                onCountChange(quantity)
                            }
                        },
                        enabled = quantity > 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.minus),
                            contentDescription = "Decrease",
                            tint = if (quantity > 1) MaterialTheme.colorScheme.onSurface else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }


                    Text(
                        text = "${quantity}",
                        style = MaterialTheme.typography.titleSmall
                    )

                    IconButton(
                        onClick = {
                            if (quantity < availableQuantity) {
                                quantity++
                                onCountChange(quantity)
                            }
                        },
                        enabled = quantity < availableQuantity,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus1),
                            contentDescription = "Increase",
                            tint = if (quantity < availableQuantity) MaterialTheme.colorScheme.onSurface else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }


                }
            }


        }
    }
}

@Composable
fun CheckoutButton(totalPrice : String ,currency: String ,navToCheckout :()->Unit,getChangeRate: Double) {
    //val totalPrice = cartItems.sumOf { it.price * it.quantity }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column {
                Text(
                    text = "Total Price",
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "${totalPrice.toDoubleOrNull()?.convertToCurrency(getChangeRate)} $currency" ,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
                onClick = {navToCheckout()}
            ) {
                Text(
                    text = "Checkout",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Checkout",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Composable
fun CustomEmpty(title:String ="Your cart is empty!" ) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = R.drawable.empty,
                contentDescription = "Empty Cart",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun CustomLoading() {
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