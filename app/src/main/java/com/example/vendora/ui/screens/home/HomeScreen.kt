package com.example.vendora.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.example.vendora.R
import com.example.vendora.domain.model.brands.SmartCollection
import com.example.vendora.domain.model.product.Product
import com.example.vendora.ui.cart_screen.viewModel.CartViewModel
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.order.OnLoading
import com.example.vendora.ui.ui_model.DialogAttributes
import com.example.vendora.ui.ui_model.GiftCardAd
import com.example.vendora.ui.ui_model.GuestModeDialog
import com.example.vendora.ui.ui_model.couponList
import com.example.vendora.utils.wrapper.Result

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    cardViewModel: CartViewModel = hiltViewModel(),
    navigateToCart: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToBrandDetails: (brandId: Long) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToCoupon: () -> Unit,
    paddingValues: PaddingValues = PaddingValues()
) {
    val brands = viewModel.brands.collectAsStateWithLifecycle()
    val username = viewModel.username.collectAsStateWithLifecycle()
    val isGuestMode = viewModel.isGuestMode.collectAsStateWithLifecycle()
    val showGuestModeDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchBrands()
        cardViewModel.checkOrCreateCart()
    }

    if (showGuestModeDialog.value) {
        GuestModeDialog(
            attributes = DialogAttributes(
                onDismiss = { showGuestModeDialog.value = false },
                onAccept = { navigateToLogin() }
            )
        )
    }

    when (brands.value) {
        is Result.Failure -> OnError {
            viewModel.fetchBrands()
            Log.d("HomeScreen", (brands.value as Result.Failure).exception.toString())
        }

        is Result.Loading -> OnLoading()
        is Result.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = rememberLazyGridState(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 8.dp, start = 12.dp, end = 12.dp)
            ) {
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    HomeHeader(
                        username = username.value,
                        navigateToCart = navigateToCart,
                        isGuestMode = isGuestMode.value,
                        showDialog = showGuestModeDialog,
                        navigateToFavorite = navigateToFavorites
                    )
                }
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    GiftCardAd(couponList,navigateToCoupon)
                }

                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Text("Our Partners", style = MaterialTheme.typography.titleLarge)
                }

                val list = (brands.value as Result.Success).data.smart_collections
                items(list, key = { it.id }) { brand ->
                    BrandCard(
                        brand,
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .clickable { navigateToBrandDetails(brand.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeHeader(
    username: String,
    navigateToCart: () -> Unit,
    navigateToFavorite: () -> Unit,
    isGuestMode: Boolean,
    showDialog: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painter = painterResource(if (isSystemInDarkTheme()) R.drawable.vendora_dark else R.drawable.vendora),
            contentDescription = "App logo",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer(
                    scaleX = 1.5f,
                    scaleY = 1.5f
                )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(
                text = "Welcome 👋",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = username.ifEmpty { "Guest" },
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            if (isGuestMode) {
                showDialog.value = true
            } else {
                navigateToFavorite()
            }
        }) {
            Icon(
                painterResource(R.drawable.favorite),
                contentDescription = "Favorite",
                modifier = Modifier.size(24.dp)
            )
        }

        IconButton(onClick = {
            if (isGuestMode) {
                showDialog.value = true
            } else {
                navigateToCart()
            }
        }) {
            Icon(
                painterResource(R.drawable.cart),
                contentDescription = "Cart",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun GiftCardAd(giftCardsList: List<GiftCardAd>,navigateToCoupon: () -> Unit) {
    // here we can replace the number with amount of gift card coming from api.
    val pagerState = rememberPagerState(pageCount = { giftCardsList.size })
    val context = LocalContext.current

    Box {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 16.dp,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Card(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .height(IntrinsicSize.Min)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .clickable { navigateToCoupon() }
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            "${giftCardsList[page].couponAmount} %",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )

                        Text(
                            giftCardsList[page].couponTitle,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            giftCardsList[page].couponDesc,
                            maxLines = 2,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(giftCardsList[page].couponImage)
                            .build(),
                        contentDescription = giftCardsList[page].couponTitle,
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                            .width(170.dp)
                    )
                }
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun BrandCard(brand: SmartCollection, modifier: Modifier) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        ),

        colors = CardDefaults
            .cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .shadow(elevation = 4.dp)
            .height(140.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(brand.image.src)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentScale = ContentScale.FillBounds,
                contentDescription = brand.title,
                modifier = Modifier
                    .weight(0.7f)
                    .background(Color.White)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.weight(0.3f)
            ) {
                Text(
                    brand.title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            AsyncImage(
                model = product.image.src,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${product.variants.firstOrNull()?.price ?: "N/A"} EGP",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}