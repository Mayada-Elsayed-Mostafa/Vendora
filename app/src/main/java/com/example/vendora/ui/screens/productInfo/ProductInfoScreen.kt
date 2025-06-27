package com.example.vendora.ui.screens.productInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vendora.R
import com.example.vendora.domain.model.product.Image
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.model.product.findVariantIdByColorAndSize
import com.example.vendora.ui.cart_screen.viewModel.CartViewModel
import com.example.vendora.ui.screens.currency.changeCurrency
import com.example.vendora.ui.screens.favorites.FavoritesViewModel
import com.example.vendora.ui.screens.search.LottieLoader
import com.example.vendora.ui.ui_model.DialogAttributes
import com.example.vendora.ui.ui_model.GuestModeDialog
import com.example.vendora.utils.wrapper.Result

@Composable
fun ProductInfoScreen(
    productId: Long,
    viewModel: ProductInfoViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {
    val productResult by viewModel.product.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    Scaffold { innerPadding ->
        when (productResult) {
            is Result.Success -> {
                val product = (productResult as Result.Success<Product>).data

                Column {
                    ProductImagesCarousel(product.images)
                    Card(
                        modifier = Modifier
                            .padding(innerPadding)
                            .statusBarsPadding()
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            ProductInfoSection(
                                viewModel = viewModel,
                                favoritesViewModel = favoritesViewModel,
                                product = product,
                                navigateToLogin = navigateToLogin
                            )
                        }
                    }
                }
            }

            is Result.Failure -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieLoader(
                        resId = R.raw.search_error,
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                    )
                }
            }

            Result.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieLoader(
                        resId = R.raw.loading_animation,
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                    )
                }
            }

        }
    }
}

@Composable
fun ProductImagesCarousel(imagesList: List<Image>) {
    val pagerState = rememberPagerState(pageCount = { imagesList.size })
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 16.dp,
            beyondViewportPageCount = 1,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imagesList[page].src)
                    .crossfade(true)
                    .build(),
                contentDescription = imagesList[page].alt,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { index ->
                val color =
                    if (pagerState.currentPage == index) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
fun ProductInfoSection(
    viewModel: ProductInfoViewModel,
    favoritesViewModel: FavoritesViewModel,
    product: Product,
    navigateToLogin: () -> Unit
) {
    val isGuestMode by viewModel.isGuestMode.collectAsStateWithLifecycle()
    val showGuestModeDialog = remember { mutableStateOf(false) }

    if (showGuestModeDialog.value) {
        GuestModeDialog(
            attributes = DialogAttributes(
                onDismiss = { showGuestModeDialog.value = false },
                onAccept = { navigateToLogin() }
            )
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        ProductInfoHeaderSection(
            product, favoritesViewModel,
            isGuestMode,
            showGuestModeDialog,
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        ProductInfoBodySection(viewModel, product)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        ProductInfoFooterSection(product)
    }
}

@Composable
fun ProductInfoHeaderSection(
    product: Product,
    favoritesViewModel: FavoritesViewModel,
    isGuestMode: Boolean,
    showDialog: MutableState<Boolean>
) {

    val favorites by favoritesViewModel.favorites.collectAsState()
    val isFavorite = favorites.any { it.id == product.id }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title,
                modifier = Modifier.weight(1f),
                fontSize = 20.sp
            )
            IconButton(onClick = {
                if (!isGuestMode) {
                    showDialog.value = true
                } else {
                    if (isFavorite) {
                        favoritesViewModel.removeFromFavorites(product.id)
                    } else {
                        favoritesViewModel.addToFavorites(product)
                    }
                }
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    tint = Color.Black
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "${product.variants[0].old_inventory_quantity - product.variants[0].inventory_quantity} Sold",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(text = product.vendor, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProductInfoBodySection(viewModel: ProductInfoViewModel, product: Product) {
    val colorOption = product.options.find { it.name.contains("color", ignoreCase = true) }
    val sizeOption = product.options.find { it.name.contains("size", ignoreCase = true) }

    val colors = colorOption?.values ?: emptyList()
    val sizes = sizeOption?.values ?: emptyList()

    val selectedColor by viewModel.selectedColor
    val selectedSize by viewModel.selectedSize
    val quantity by viewModel.quantity

    LaunchedEffect(Unit) {
        viewModel.initializeOptions(colors, sizes)
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column {
            Text(
                "Description",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(product.body_html, fontSize = 12.sp)
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (sizes.isNotEmpty()) {
                Column {
                    Text(
                        "Size",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        sizes.forEach { size ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (selectedSize == size) Color.Gray else Color.LightGray)
                                    .clickable { viewModel.onSizeSelected(size) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(text = size, color = Color.White)
                            }
                        }
                    }
                }
            }

            if (colors.isNotEmpty()) {
                Column {
                    Text(
                        "Color",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        try {
                                            Color(color.toColorInt())
                                        } catch (e: Exception) {
                                            Color.LightGray
                                        }
                                    )
                                    .border(
                                        width = if (selectedColor == color) 2.dp else 1.dp,
                                        color = if (selectedColor == color) Color.Black else Color.Gray,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.onColorSelected(color) }
                            )
                        }
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Quantity", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Button(onClick = {
                viewModel.decreaseQuantity()
            }) { Text("-") }
            Text("$quantity", fontSize = 20.sp)
            Button(onClick = {
                viewModel.increaseQuantity()
            }) { Text("+") }
        }
    }
}

@Composable
fun ProductInfoFooterSection(
    product: Product,
    viewModel: ProductInfoViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState by cartViewModel.uiState.collectAsState()
    val quantity by viewModel.quantity
    val selectedSize by viewModel.selectedSize
    val selectedColor by viewModel.selectedColor
    val context = LocalContext.current
    val isGuestMode by viewModel.isGuestMode.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Total Price", fontSize = 14.sp, color = Color.Gray)
            Text(
                (product.variants[0].price.toDouble() * quantity).changeCurrency(context),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Button(
            enabled = !uiState.isAddingToCart,
            onClick = {
                if (!isGuestMode){
                    return@Button
                }
                cartViewModel.addToCart(
                    product.findVariantIdByColorAndSize(
                        selectedColor,
                        selectedSize
                    ) ?: product.variants[0].admin_graphql_api_id, quantity
                )
            }, modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            if (uiState.isAddingToCart) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Add to cart",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Add to cart", fontSize = 16.sp)
            }


        }
    }
}
