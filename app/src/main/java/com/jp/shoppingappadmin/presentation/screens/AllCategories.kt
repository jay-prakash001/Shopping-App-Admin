package com.jp.shoppingappadmin.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingappadmin.domain.model.CategoryModel
import com.jp.shoppingappadmin.presentation.navigation.SearchProductsByCategory
import com.jp.shoppingappadmin.presentation.utils.ShimmerEffectBox
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import java.util.Locale
import kotlin.math.truncate

@Composable
fun AllCategories(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(), navController: NavController
) {

    val categories = viewModel.categories.collectAsStateWithLifecycle().value
    LazyVerticalGrid(
        modifier = Modifier.padding(top = 10.dp),
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(categories) {
            CategoryBox(
                modifier = Modifier.padding(5.dp),
                category = it,
                viewModel = viewModel,
                navController = navController
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBox(
    modifier: Modifier = Modifier,
    category: CategoryModel,
    viewModel: ShoppingAppViewModel, navController: NavController
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(5.dp),
        colors = CardDefaults.cardColors(Color.Transparent),
        elevation = CardDefaults.elevatedCardElevation(5.dp), onClick = {
            navController.navigate(SearchProductsByCategory(category.name))
        }

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = category.img,
                contentDescription = category.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                loading = {
                    ShimmerEffectBox()
                }, contentScale = ContentScale.Crop
            )

            Text(
                text = category.name.capitalize(Locale.ROOT),
                fontWeight = FontWeight.Black,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
            Text(
                text = category.createdBy.capitalize(Locale.ROOT),
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )



            IconButton(onClick = {
                Toast.makeText(context, viewModel.deleteCategory(category), Toast.LENGTH_SHORT)
                    .show()
                viewModel.getCategories()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete ${category.name} category"
                )
            }

        }
    }
}
