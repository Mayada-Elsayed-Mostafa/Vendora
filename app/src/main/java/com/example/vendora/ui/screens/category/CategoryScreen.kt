package com.example.vendora.ui.screens.category

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vendora.R
import com.example.vendora.domain.model.product.Product
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.search.ProductCard
import com.example.vendora.ui.screens.order.OnLoading
import com.example.vendora.ui.ui_model.DialogAttributes
import com.example.vendora.ui.ui_model.GuestModeDialog
import com.example.vendora.ui.ui_model.subCategories
import com.example.vendora.ui.ui_model.tabs
import com.example.vendora.utils.wrapper.Result
import com.example.vendora.utils.wrapper.isGuestMode

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    navigateToProductInfo: (Long) -> Unit,
    navigateToCart: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToLogin: () -> Unit
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val showGuestModeDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.collectCategories()
    }

    if (showGuestModeDialog.value) {
        GuestModeDialog(
            attributes = DialogAttributes(
                onDismiss = { showGuestModeDialog.value = false },
                onAccept = { navigateToLogin() }
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        topBar = {
            CategoryAppBar(
                result = emptyList(),
                onSearchQueryChange = { query -> println(query) },
                navigateToCart = {
                    if (viewModel.isGuestMode()) {
                        showGuestModeDialog.value = true
                    } else {
                        navigateToCart()
                    }
                },
                navigateToFavorite = {
                    if (viewModel.isGuestMode()) {
                        showGuestModeDialog.value = true
                    } else {
                        navigateToFavorite()
                    }
                }
            )
        },
        floatingActionButton = {
            SubCategoryFAB { title ->
                viewModel.applySubCategory(title)
            }
        }
    ) { innerPadding ->

        when (state.value.categoryResult) {
            is Result.Failure -> OnError { viewModel.startCollection() }
            Result.Loading -> OnLoading()
            is Result.Success -> {
                OnSuccess(
                    paddingValues = innerPadding,
                    onCategoryChange = { category -> viewModel.onCategoryChanged(title = category) },
                    products = state.value.filteredProducts,
                    currentCategory = state.value.subCategory,
                    navigateToProductInfo = navigateToProductInfo
                )
            }
        }
    }
}

@Composable
fun OnSuccess(
    paddingValues: PaddingValues = PaddingValues(),
    onCategoryChange: (String) -> Unit,
    products: List<Product>,
    navigateToProductInfo: (Long) -> Unit,
    currentCategory: String
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState)
    ) {
        CategoryTabs(
            onCategoryChange,
            currentCategory
        )
        ProductsGrid(
            modifier = Modifier.weight(1f),
            products = products,
            navigateToProductInfo = navigateToProductInfo
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryAppBar(
    result: List<String>,
    onSearchQueryChange: (String) -> Unit,
    navigateToCart: () -> Unit,
    navigateToFavorite: () -> Unit
) {
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
                IconButton(onClick = {
                    navigateToFavorite()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.favorite),
                        contentDescription = "Favorite",
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { navigateToCart() }) {
                    Icon(
                        painter = painterResource(R.drawable.cart),
                        contentDescription = "cart",
                        modifier = Modifier.size(24.dp)
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
    onCategoryChange: (String) -> Unit,
    currentCategory: String
) {
    var currentIndex by remember { mutableIntStateOf(tabs.indexOf(currentCategory)) }
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
    products: List<Product>,
    navigateToProductInfo: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(8.dp)
    ) {
        items(
            items = products,
            key = { item: Product -> item.id }
        ) {
            ProductCard(it, navigateToProductInfo)
        }
    }
}

@Composable
fun SubCategoryFAB(
    onSubCategoryClicked: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val contentColor = if (isSystemInDarkTheme()) Color.Black else Color.White

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentWidth()
    ) {

        if (expanded) {
            subCategories.forEach { category ->
                SmallFloatingActionButton(
                    onClick = {
                        onSubCategoryClicked(category.name)
                    },
                    contentColor = contentColor,
                    containerColor = containerColor
                ) {
                    Icon(
                        painter = painterResource(category.icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        FloatingActionButton(
            onClick = {
                expanded = !expanded
                if (!expanded) {
                    onSubCategoryClicked("")
                }
            },
            contentColor = contentColor,
            containerColor = containerColor
        ) {
            if (expanded) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.filter),
                    contentDescription = "Filter"
                )
            }

        }
        Spacer(modifier = Modifier.height(4.dp))
    }

}