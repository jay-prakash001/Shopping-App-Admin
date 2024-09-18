package com.jp.shoppingappadmin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jp.shoppingappadmin.presentation.screens.AddProductsScreen
import com.jp.shoppingappadmin.presentation.screens.AllCategories
import com.jp.shoppingappadmin.presentation.screens.ProductDetailScreen
import com.jp.shoppingappadmin.presentation.screens.ProductsByCategoryScreen
import com.jp.shoppingappadmin.presentation.screens.SearchScreen
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier, viewModel:ShoppingAppViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = DashBoard) {
        composable<DashBoard> {
            App(navController = navController)
        }
        composable<Category> {
            AllCategories(navController = navController)
        }
        composable<AddProducts> {
            AddProductsScreen()
        }
        composable<SearchProducts> {
            val name = it.toRoute<SearchProducts>().name
            SearchScreen(name = name, navController = navController)

        }
        composable<SearchProductsByCategory> {
            val name = it.toRoute<SearchProductsByCategory>().name
            ProductsByCategoryScreen(category = name)

        }
        composable<ProductDetail> {
            val id = it.toRoute<ProductDetail>()

            val listOfProducts =
                viewModel.products.collectAsState().value.filter { it.id == id.id }
            if (listOfProducts.isNotEmpty()) {

                val product = listOfProducts[0]
                val suggestedProducts =
                    viewModel.products.collectAsStateWithLifecycle().value.filter { it ->
                        it.category == product.category && it.id != product.id
                    }
                ProductDetailScreen(
                    product = product,
                    navController = navController,
                    suggestedProducts = suggestedProducts,
                    viewModel = viewModel
                )
            }

        }

    }
}