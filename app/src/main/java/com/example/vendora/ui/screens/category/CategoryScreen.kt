package com.example.vendora.ui.screens.category

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.model.product.Products
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.brandDetails.OnLoading
import com.example.vendora.ui.screens.brandDetails.ProductCard
import com.example.vendora.ui.screens.brandDetails.TopScreenSearchBar
import com.example.vendora.ui.ui_model.tabs
import com.example.vendora.utils.wrapper.Result

@Composable
fun CategoryScreen(viewModel: CategoryViewModel = hiltViewModel()) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        viewModel.collectCategories()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        topBar = {
            CategoryAppBar(
                result = emptyList(),
                onSearchQueryChange = { query -> println(query) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->

        when(state.value.categoryResult){
            is Result.Failure -> OnError{viewModel.startCollection()}
            Result.Loading -> OnLoading()
            is Result.Success -> {
                OnSuccess(
                    paddingValues = innerPadding,
                    onCategoryChange = {category -> viewModel.onCategoryChanged(title = category)},
                    products = state.value.filteredProducts
                )
            }
        }
    }
}

@Composable
fun OnSuccess(
    paddingValues: PaddingValues = PaddingValues(),
    onCategoryChange: (String) -> Unit,
    products: List<Product>
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState)
    ){
        CategoryTabs(onCategoryChange)
        ProductsGrid(
            modifier = Modifier.weight(1f),
            products = products
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAppBar(result: List<String>, onSearchQueryChange:(String) -> Unit ) {
    var searchQuery by remember { mutableStateOf("") }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        placeholder = { Text("search for product") },
        trailingIcon = {
            Row {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Favorite"
                    )
                }

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Favorite"
                    )
                }
            }
        },
        query = searchQuery,
        shape = SearchBarDefaults.fullScreenShape,
        onQueryChange = { searchQuery = it },
        onSearch = {},
        active = false,
        onActiveChange = {}
    ) { }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTabs(
    onCategoryChange: (String) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    SecondaryTabRow(
        selectedTabIndex = currentIndex,
        modifier = Modifier.fillMaxWidth(),
        indicator = {
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(currentIndex),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        tabs.forEachIndexed { index, name ->
            Tab(
                selected = index == currentIndex,
                onClick = {
                    currentIndex = index
                    Log.d("Category",name)
                    onCategoryChange(name)
                },
                text = { Text(name) },
                selectedContentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ProductsGrid(
    modifier: Modifier = Modifier,
    products: List<Product>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(
            items = products,
            key = {item: Product -> item.id }
        ){
            ProductCard(it)
        }
    }
}
