package com.example.vendora.ui.screens.brandDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.vendora.R
import com.example.vendora.domain.model.product.Image
import com.example.vendora.domain.model.product.Option
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.model.product.Variant
import com.example.vendora.utils.wrapper.Result

@Composable
fun BrandDetailsScreen(
    id: Long = 450846785767,
    navigateUp: () -> Unit,
    viewModel: BrandDetailsViewModel = hiltViewModel()
) {

    val products = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getProductsByBrandId(id)
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
        )

        when (products.value.products) {
            is Result.Failure -> OnError()
            Result.Loading -> OnLoading()
            is Result.Success -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Products",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
                val list = products.value.filteredProducts
                OnSuccess(list)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreenSearchBar(
    onInputQuery: (query: String) -> Unit,
    navigateUp: () -> Unit,
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
                    onClick = {}
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
fun OnSuccess(products: List<Product>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = rememberLazyGridState(),
    ) {
        items(
            items = products,
            key = { item -> item.id }
        ) { product ->
            ProductCard(product)
        }
    }
}

@Composable
fun OnError(modifier: Modifier = Modifier) {

}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {

}

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
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

@Preview
@Composable
private fun TopScreenSearchBarPreview() {
    TopScreenSearchBar(
        onInputQuery = { query ->
            println(query)
        },
        navigateUp = {},
    )
}