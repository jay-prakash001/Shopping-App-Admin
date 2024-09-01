package com.jp.shoppingappadmin.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jp.shoppingappadmin.presentation.utils.CategoryCircleBox
import com.jp.shoppingappadmin.presentation.utils.CategoryList
import com.jp.shoppingappadmin.presentation.utils.ProductsBox
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    name: String,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(key1 = true) {
        viewModel.searchProducts(name)
        viewModel.searchCategories(name)

    }


    val searchInput = remember {
        mutableStateOf(name)
    }

    val products = viewModel.products.collectAsStateWithLifecycle().value
    val categories = viewModel.categories.collectAsStateWithLifecycle().value
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Search For A Product") }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            })
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = searchInput.value,
                onValueChange = {
                    searchInput.value = it
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.searchProducts(searchInput.value)
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                label = {
                    Text(text = "Search ")
                })
            CategoryList(categories = categories, navController = navController)
            LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
                items(products) { product ->
                    ProductsBox(product = product)
                }

                item {
                    if (products.isEmpty()) {
                        Text(text = "No Product Found!")
                    }
                }
            }
        }
    }
}