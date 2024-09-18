package com.jp.shoppingappadmin.presentation.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingappadmin.domain.model.CategoryModel
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.presentation.navigation.AddProducts
import com.jp.shoppingappadmin.presentation.navigation.Category
import com.jp.shoppingappadmin.presentation.navigation.SearchProducts
import com.jp.shoppingappadmin.presentation.navigation.SearchProductsByCategory
import com.jp.shoppingappadmin.presentation.screens.AddProductsScreen
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import java.util.Locale
import kotlin.random.Random

@Composable
fun ShimmerEffectBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Gray.copy(alpha = 0.3f),
                        Color.Gray.copy(alpha = 0.7f),
                        Color.Gray.copy(alpha = 0.3f)
                    ),
                    start = Offset(translateAnim, 0f),
                    end = Offset(translateAnim + 1000f, 100f)
                )
            )
    )
}


@Composable
fun AlphaBox(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.background(Color.Gray.copy(.5f)), contentAlignment = Alignment.Center) {
        content()
    }
}

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    categories: List<CategoryModel>,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxWidth()) {


        LazyRow(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(categories) { index, item ->
                CategoryCircleBox(category = item, navController = navController)

            }
        }


        Text(
            text = "See all",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { navController.navigate(Category) })

    }


}

@Composable
fun CategoryCircleBox(
    modifier: Modifier = Modifier,
    category: CategoryModel,
    navController: NavController
) {
    Column(
        modifier = modifier
            .padding(2.dp)
            .clickable {
                navController.navigate(SearchProductsByCategory(category.name))
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SubcomposeAsyncImage(
            model = category.img,
            contentDescription = category.name,
            loading = {
                ShimmerEffectBox()
            },
            modifier = Modifier
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .size(50.dp), contentScale = ContentScale.Crop
        )
        Text(
            text = category.name.capitalize(Locale.ROOT),
            style = MaterialTheme.typography.labelSmall
        )
    }
}


@Composable
fun CategoryShimmer(modifier: Modifier = Modifier) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {

        items(6) {
            Column(
                modifier = modifier.padding(2.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(60.dp)
                        .height(10.dp)
                        .shimmerEffect()
                )
            }

        }

    }
}

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    products: List<ProductModel>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {

        LazyRow(

        ) {
            items(products) {
                ProductsBox(product = it, modifier = modifier)
            }

        }
        Text(
            text = "See all",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { navController.navigate(AddProducts) })

    }
}

@Composable
fun ProductsShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow {
            items(20) {
                val cornerShape = RoundedCornerShape(10.dp)
                val padding = 2.dp

                Column(
                    modifier = modifier
                        .padding(2.dp)
                        .fillMaxWidth(.7f)
                        .height(150.dp)
                        .clip(cornerShape)
                        .padding(padding)
                        .shimmerEffect(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.width(100.dp))
                }

            }

        }
    }
}


@Composable
fun ProductsBox(
    modifier: Modifier = Modifier,
    product: ProductModel, viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val borderColor = Color(0xFFBBB9B9)
    val cornerShape = RoundedCornerShape(10.dp)
    val padding = 5.dp
    val random = Random.nextInt(0, 100) % 2 == 0
    val height = if (random) 180.dp else 170.dp
    val randomColor = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), .1f)
    Column(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(height)
            .border(1.dp, borderColor, cornerShape)
            .clip(cornerShape)
            .background(randomColor)
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val thumbnail = if (product.images.isNotEmpty()) product.images[0] else ""
       Box(modifier = Modifier.size(100.dp)){

           SubcomposeAsyncImage(
               model = thumbnail,
               contentDescription = product.name,
               modifier = Modifier
                   .fillMaxWidth().clip(RoundedCornerShape(10.dp))
                   ,
               loading = {
                   ShimmerEffectBox()
               },
               contentScale = ContentScale.Crop
           )
           IconButton(onClick = { viewModel.deleteProduct(product) }, modifier = Modifier.align(
               Alignment.TopEnd).size(20.dp),colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.secondary.copy(.5f))) {
               Icon(imageVector = Icons.Default.Close, contentDescription = "delete Product", tint = MaterialTheme.colorScheme.onSecondary)
           }
       }

        Text(
            text = product.name.capitalize(Locale.ROOT),
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        Text(
            text = product.desc.capitalize(Locale.ROOT),
            fontWeight = FontWeight.Light,
            fontSize = MaterialTheme.typography.labelSmall.fontSize
        )
        Text(
            text = "${product.price}/-",
            fontWeight = FontWeight.Normal,
            fontSize = MaterialTheme.typography.labelLarge.fontSize
        )
    }
}

@Composable
fun ProductsGrid(modifier: Modifier = Modifier, products: List<ProductModel>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        modifier = modifier
    ) {
        items(products) { product ->
            ProductsBox(product = product)
        }

    }
}



@Composable
fun SearchField(navController: NavController) {
    val searchInput = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = searchInput.value,
        onValueChange = {
            searchInput.value = it
        },
        trailingIcon = {
            IconButton(onClick = {
                navController.navigate(SearchProducts(searchInput.value))

            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search")
            }
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        label = {
            Text(text = "Search")
        })
}