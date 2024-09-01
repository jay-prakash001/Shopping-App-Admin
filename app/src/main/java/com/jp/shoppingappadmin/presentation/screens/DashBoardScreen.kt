package com.jp.shoppingappadmin.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jp.shoppingappadmin.R
import com.jp.shoppingappadmin.domain.model.CategoryModel
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.presentation.navigation.AddProducts
import com.jp.shoppingappadmin.presentation.navigation.SearchProducts
import com.jp.shoppingappadmin.presentation.utils.BannerRow
import com.jp.shoppingappadmin.presentation.utils.CategoryList
import com.jp.shoppingappadmin.presentation.utils.CategoryShimmer
import com.jp.shoppingappadmin.presentation.utils.ProductsList
import com.jp.shoppingappadmin.presentation.utils.ProductsShimmer
import com.jp.shoppingappadmin.presentation.utils.SearchField
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.getProducts()
        viewModel.getCategories()
        viewModel.getAllBanners()
    }

    Scaffold(topBar = {

        TopAppBar(title = {
            Text(
                text = "DashBoard",
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }, navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.app),
                contentDescription = "app logo",
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
            )
        })

    }) {
        Box(modifier = Modifier.padding(it), contentAlignment = Alignment.Center) {

            DashScreen(navController = navController)

        }
    }

}

@Composable
fun DashScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavController
) {
    val categories = viewModel.categories.collectAsStateWithLifecycle().value
    val products = viewModel.products.collectAsStateWithLifecycle().value
    val banners = viewModel.banners.collectAsStateWithLifecycle().value
    Column(modifier = Modifier.fillMaxSize()) {


        SearchField(navController)

        Spacer(modifier = Modifier.height(10.dp))
        CategoryRow(categories, navController)
        BannerRow(banners = banners, navController = navController)

        ProductsRow(products, navController)

    }


}


@Composable
private fun ColumnScope.ProductsRow(
    products: List<ProductModel>,
    navController: NavController
) {
    AnimatedVisibility(visible = products.isEmpty()) {
        ProductsShimmer()
    }


    AnimatedVisibility(visible = products.isNotEmpty()) {
        ProductsList(
            products = products,
            modifier = Modifier.fillMaxHeight(.62f),
            navController = navController
        )
    }
}

@Composable
private fun ColumnScope.CategoryRow(
    categories: List<CategoryModel>,
    navController: NavController
) {
    AnimatedVisibility(visible = categories.isEmpty()) {
        CategoryShimmer()
    }
    AnimatedVisibility(visible = categories.isNotEmpty()) {
        CategoryList(categories = categories, navController = navController)
    }
}