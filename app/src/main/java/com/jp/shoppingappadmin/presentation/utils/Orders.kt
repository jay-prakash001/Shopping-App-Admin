package com.jp.shoppingappadmin.presentation.utils


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingappadmin.domain.model.OrderModel
import com.jp.shoppingappadmin.domain.model.OrderParentModel
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.presentation.navigation.ProductDetail
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderProductCard(
    modifier: Modifier = Modifier,
    orderParentModel: OrderParentModel,
    viewModel: ShoppingAppViewModel,
    navController: NavHostController,
    onUpdate: (String) -> Unit = {}
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }
    val isCancelled = orderParentModel.status.contains("Cancelled")
    val isDelivered = orderParentModel.status.contains("Delivered")

    val color = animateColorAsState(
        targetValue = if (isCancelled) Color.Red else if (isDelivered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .background(if (isCancelled || isDelivered) color.value.copy(.1f) else MaterialTheme.colorScheme.background)
            .border(
                2.dp,
                if (isCancelled || isDelivered) color.value else Color.LightGray,
                RoundedCornerShape(10.dp)
            ),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End
    ) {
        orderParentModel.order?.let {
            CartProductsBox(
                orderModel = it,
                viewModel = viewModel,
                navController = navController
            )



            DeliveryDetails(orderParentModel = orderParentModel)
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Status")
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "see all")
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
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

                Column {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (isCancelled) Arrangement.SpaceBetween else Arrangement.spacedBy(
                            6.dp
                        )
                    ) {
                        orderParentModel.status.forEach {

                            Text(
                                text = it,
                                fontSize = if (isCancelled) 10.sp else 8.sp,
                                color = MaterialTheme.colorScheme.onError,
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(50.dp)
                                    )
                                    .background(color = color.value)

                            )
                        }
                    }
                    Slider(
                        value = (orderParentModel.status.size - 1).toFloat(),
                        onValueChange = {},
                        valueRange = 0f..if (isCancelled) 1f else 5f,
                        modifier = Modifier,
                        colors = SliderDefaults.colors(
                            activeTrackColor = color.value,
                            thumbColor = color.value
                        )
                    )
                    var isMenuExpanded by remember { mutableStateOf(false) }  // Mutable state for expanded state
                    var status by remember { mutableStateOf("") }  // Mutable state for status text

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isMenuExpanded,
                            onExpandedChange = {
                                isMenuExpanded = !isMenuExpanded
                            },  // Toggle menu on click
                            modifier = Modifier.fillMaxWidth(.6f)
                        ) {
                            TextField(
                                value = status,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.menuAnchor(),
                                placeholder = {
                                    Text(text = "Status")
                                },
                                trailingIcon = {

                                    Icon(
                                        imageVector = if (isMenuExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = "see all"
                                    )

                                }
                            )
                            ExposedDropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Packed") },
                                    onClick = {
                                        status = "Packed"
                                        isMenuExpanded = false  // Close menu after selection
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Shipped") },
                                    onClick = {
                                        status = "Shipped"
                                        isMenuExpanded = false  // Close menu after selection
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "In Transit") },
                                    onClick = {
                                        status = "In Transit"
                                        isMenuExpanded = false  // Close menu after selection
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Ware house") },
                                    onClick = {
                                        status = "Ware house"
                                        isMenuExpanded = false  // Close menu after selection
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Out For Delivery") },
                                    onClick = {
                                        status = "Out For Delivery"
                                        isMenuExpanded = false  // Close menu after selection
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Delivered") },
                                    onClick = {
                                        status = "Delivered"
                                        isMenuExpanded = false  // Close menu after selection
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Cancelled", color = Color.Red) },
                                    onClick = {
                                        status = "Cancelled"
                                        isMenuExpanded = false  // Close menu after selection
                                    }
                                )
                            }
                        }
                        Button(
                            onClick = {
                                onUpdate(status)
                            },
                            enabled = !(orderParentModel.status.contains("Cancelled") || orderParentModel.status.contains(
                                "Delivered"
                            ) || status.isBlank()),

                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Text(text = "Update")
                        }
                    }


                }
            }
        }
    }


}


@Composable
fun CartProductsBox(
    modifier: Modifier = Modifier,
    orderModel: OrderModel,
    viewModel: ShoppingAppViewModel,
    navController: NavHostController
) {
    Column(modifier = modifier.padding(5.dp)) {
        orderModel.product?.let {
            ProductCardRow(
                product = it,
                viewModel = viewModel,
                navController = navController
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity : ${orderModel.quantity}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Total Price : ${orderModel.quantity} * ${orderModel.product.price} = ${orderModel.quantity * orderModel.product.price}Rs.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCardRow(
    modifier: Modifier = Modifier,
    product: ProductModel,
    viewModel: ShoppingAppViewModel,
    navController: NavController
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(150.dp, 200.dp), onClick = {
            navController.navigate(ProductDetail(product.id))
        }, colors = CardDefaults.cardColors(Color.White.copy(.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SubcomposeAsyncImage(
                model = product.images[0], contentDescription = product.name, modifier = Modifier
                    .size(120.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    ), contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontWeight = FontWeight.Light,
                    fontSize = 15.sp
                )
                Text(
                    text = "Rs.${product.price}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }

        }
    }
}


@Composable
fun DeliveryDetails(orderParentModel: OrderParentModel, modifier: Modifier = Modifier) {
    var show by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().clickable { show = !show },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
                Text(text = "Name : ${orderParentModel.name} ")
                Text(text = "Note : ${orderParentModel.desc} ")
        AnimatedVisibility(
            visible = show,
            enter = fadeIn(tween(500)) + scaleIn(tween(500)),
            exit = fadeOut(animationSpec = tween(500)) + scaleOut(
                tween(500)
            )) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Address : ${orderParentModel.address} ")
                Text(text = "Email : ${orderParentModel.email} ")
                Text(text = "Contact : ${orderParentModel.contact} ")
            }

        }
        }



    }
}