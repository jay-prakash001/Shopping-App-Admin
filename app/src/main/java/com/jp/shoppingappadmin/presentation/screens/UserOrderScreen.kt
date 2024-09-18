package com.jp.shoppingappadmin.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingappadmin.domain.model.User
import com.jp.shoppingappadmin.presentation.utils.OrderProductCard
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserOrderScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getUsers()

    }
    val users = viewModel.users.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {

            TopAppBar(title = {
                Text(
                    text = "Clients and orders",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }, actions = {

            })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(users) {
                UserListItem(user = it, viewModel = viewModel, navController = navController)
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListItem(
    modifier: Modifier = Modifier,
    user: User,
    viewModel: ShoppingAppViewModel,
    navController: NavHostController
) {
    var showOrders by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Card(
        onClick = {
            viewModel.getOrders(user.email)
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = user.profileImg,
                contentDescription = user.name,
                loading = { CircularProgressIndicator() }, modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Text(
                    text = user.email.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontWeight = FontWeight.Light,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {

                IconButton(onClick = {
                    viewModel.getOrders(user.email)
                    scope.launch {
                        delay(800)
                    showOrders = !showOrders
                    }
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        imageVector = if (showOrders) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Show Orders"
                    )
                }
            }


        }

        AnimatedVisibility(
            visible = showOrders,
            enter = fadeIn(
                animationSpec = tween(500)
            ) + scaleIn(
                animationSpec = tween(500)
            ),
            exit = fadeOut(
                animationSpec = tween(500)
            ) + scaleOut(
                animationSpec = tween(500)
            )
        ) {
            val orders = viewModel.orders.collectAsStateWithLifecycle().value
            LazyColumn(
                modifier = Modifier.padding(bottom = 80.dp)
                    .fillMaxWidth()
                    .heightIn(0.dp, 800.dp)
            ) {
                items(orders) {orderParentModel->
                    OrderProductCard(
                        orderParentModel = orderParentModel,
                        viewModel = viewModel,
                        navController = navController
                    ){
                        viewModel.updateOrder(user.email,orderParentModel,
                            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
                    }
                }
            }
        }
    }
}