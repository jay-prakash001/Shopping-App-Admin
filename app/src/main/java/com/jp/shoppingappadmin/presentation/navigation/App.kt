package com.jp.shoppingappadmin.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jp.shoppingappadmin.common.NavigationItem
import com.jp.shoppingappadmin.presentation.screens.AddProductsScreen
import com.jp.shoppingappadmin.presentation.screens.CategoryScreen
import com.jp.shoppingappadmin.presentation.screens.DashBoardScreen
import com.jp.shoppingappadmin.ui.theme.cyan
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(modifier: Modifier = Modifier, navController: NavController) {
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }
    val navList = listOf(
        NavigationItem("Dashboard", Icons.Default.Dashboard),
        NavigationItem("Category", Icons.Default.Category),
        NavigationItem("Notifications", Icons.Default.Notifications),
        NavigationItem("Product", Icons.Default.Add),
        NavigationItem("Order", Icons.Default.ShoppingCart)
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(
                        1.dp,
                        color = cyan,
                        RoundedCornerShape(

                            bottomEnd = 30.dp,
                            bottomStart = 30.dp
                        )
                    ),
//                containerColor = Color.Transparent,// Use transparent color to handle custom background
            ) {
                navList.forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        selected = index == selectedIndex.intValue,
                        onClick = { selectedIndex.intValue = index },
                        icon = {
                            Icon(
                                imageVector = navigationItem.icon,
                                contentDescription = navigationItem.name
                            )
                        },
                        label = {
                            Text(
                                text = navigationItem.name.capitalize(Locale.ROOT),
                                fontSize = 8.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.DarkGray,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color.White,
                            selectedTextColor = Color.DarkGray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier
                            .padding(0.dp) // Remove extra padding
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            when (selectedIndex.intValue) {
                0 -> {
                    DashBoardScreen(navController = navController)
                }

                1 -> {
                    CategoryScreen(navController = navController)
                }

                2 -> {
                    Text(text = "notification")
                }

                3 -> {
                    AddProductsScreen()
                }

                4 -> {
                    Text(text = "orders")
                }
            }

        }
    }
}




