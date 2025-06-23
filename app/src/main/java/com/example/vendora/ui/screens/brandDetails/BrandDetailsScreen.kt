package com.example.vendora.ui.screens.brandDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.vendora.R
import com.example.vendora.domain.model.product.Product
import com.example.vendora.ui.screens.currency.convertToCurrency
import com.example.vendora.ui.screens.order.OnLoading
import com.example.vendora.utils.wrapper.Result
import kotlin.math.roundToInt

@Composable
fun BrandDetailsScreen(
    id: Long = 450846785767,
    navigateUp: () -> Unit,
    navigateToProduct: (Long) -> Unit,
    viewModel: BrandDetailsViewModel = hiltViewModel()
) {

    val products = viewModel.uiState.collectAsStateWithLifecycle()
    val showPriceFilterDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getProductsByBrandId(id)
    }

    if (showPriceFilterDialog.value) {
        PriceSlider(
            resetFilter = { viewModel.resetProducts() },
            onSliderChange = { maxPrice ->
                viewModel.filterProductsByPrice(maxPrice)
                showPriceFilterDialog.value = false
            },
            onDismiss = {
                viewModel.resetProducts()
                showPriceFilterDialog.value = false
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TopScreenSearchBar(
            onInputQuery = { query ->
                viewModel.filterProducts(query)
            },
            navigateUp = navigateUp,
            priceFilter = showPriceFilterDialog
        )

        when (products.value.products) {
            is Result.Failure -> OnError(getProducts = { viewModel.getProductsByBrandId(id) })
            Result.Loading -> OnLoading()
            is Result.Success -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Products",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
                val list = products.value.filteredProducts
                OnSuccess(
                    list,
                    navigateToProduct = navigateToProduct
                )
            }
        }
    }
}

@Composable
fun PriceSlider(
    resetFilter: () -> Unit,
    onSliderChange: (Float) -> Unit,
    onDismiss: () -> Unit,
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Dialog(
        onDismissRequest = resetFilter,
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
        ),
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.wrapContentHeight()
                    .padding(16.dp)
            ) {
                Text(
                    "Filter by price",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))
                Slider(
                    value = sliderPosition,
                    valueRange = 0f..500f,
                    onValueChange = { sliderPosition = it }
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    sliderPosition.roundToInt().toString(),
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(
                            "reset",
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSliderChange(sliderPosition)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    ) {
                        Text("apply")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreenSearchBar(
    onInputQuery: (query: String) -> Unit,
    navigateUp: () -> Unit,
    priceFilter: MutableState<Boolean>
) {
    var searchQuery by remember { mutableStateOf("") }
    val expanded by remember { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        leadingIcon = {
            IconButton(
                onClick = { navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        placeholder = { Text("search for product") },
        shadowElevation = 4.dp,
        query = searchQuery,
        onQueryChange = {
            searchQuery = it
            onInputQuery(it)
        },
        active = expanded,
        onActiveChange = {},
        onSearch = {},
        shape = MaterialTheme.shapes.medium,
        trailingIcon = {
            Row {
                IconButton(
                    onClick = {
                        priceFilter.value = !priceFilter.value
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.filter),
                        contentDescription = null,
                    )
                }
            }
        }
    ) {}
}

@Composable
fun OnSuccess(
    products: List<Product>,
    navigateToProduct: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = rememberLazyGridState(),
    ) {
        items(
            items = products,
            key = { item -> item.id }
        ) { product ->
            ProductCard(product, navigateToProduct)
        }
    }
}

@Composable
fun OnError(getProducts: () -> Unit = {}) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(painter = painterResource(R.drawable.connection), contentDescription = null)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Something went wrong")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { getProducts() }) { Text("Try again") }
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ProductCard(
    product: Product,
    navigateToProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        onClick = { navigateToProduct(product.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(product.image.src)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = product.title,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .weight(0.6f)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                product.title.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "\$${product.variants[0].price}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                maxLines = 1,
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth()
            )
        }
    }
}