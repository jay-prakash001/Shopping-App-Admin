package com.jp.shoppingappadmin.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jp.shoppingappadmin.presentation.screens.AddProductsScreen
import com.jp.shoppingappadmin.presentation.screens.AllCategories
import com.jp.shoppingappadmin.presentation.screens.ProductsByCategoryScreen
import com.jp.shoppingappadmin.presentation.screens.SearchScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
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


    }
}