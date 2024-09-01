package com.jp.shoppingappadmin.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jp.shoppingappadmin.presentation.utils.ProductsBox
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel

@Composable
fun ProductsByCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(), category: String
) {
    LaunchedEffect(Unit) {
        viewModel.getProductsByCategory(category)
    }
    val products = viewModel.products.collectAsStateWithLifecycle().value

    LazyVerticalGrid(columns = GridCells.Adaptive(100.dp), modifier = Modifier.fillMaxSize()) {
        items(products) { product ->
            ProductsBox(product = product)
        }
    }
}