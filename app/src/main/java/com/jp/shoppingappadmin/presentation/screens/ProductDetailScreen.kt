package com.jp.shoppingappadmin.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    product: ProductModel, navController: NavHostController,
    viewModel: ShoppingAppViewModel,
    suggestedProducts: List<ProductModel>
) {

    Scaffold(
        topBar = {
            TopAppBar(title = {

                Text(text = product.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                })
            },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.size(50.dp),
                        colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "add to wishList",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                        },
                        modifier = Modifier.size(50.dp),
                        colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "edit",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        )
                    }

                }
            )
        }, modifier = Modifier.fillMaxSize()

    ) {


    }


}