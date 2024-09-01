package com.jp.shoppingappadmin.presentation.utils

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingappadmin.R
import com.jp.shoppingappadmin.common.BANNER_IMG
import com.jp.shoppingappadmin.domain.model.Banner
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random


@Composable
fun BannerRow(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    banners: List<Banner>,
    navController: NavController
) {
    val state = viewModel.bannerState.collectAsStateWithLifecycle().value

    if (state.isLoading) {
        BannerShimmer()


    } else {

        if (banners.isEmpty()) {
            AddBanner(modifier = modifier, navController = navController, viewModel = viewModel)
        } else {
            var isAdding by remember {
                mutableStateOf(false)
            }
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (isAdding) {


                    AddBanner(
                        modifier = Modifier
                            .width(350.dp)
                            .height(200.dp),
                        navController = navController,
                        viewModel = viewModel
                    )
                } else {

                    InfiniteSlidingLazyRow(banners = banners, navController, viewModel)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            isAdding = !isAdding
                        })
                }

            }


        }

    }


}

@Composable
fun InfiniteSlidingLazyRow(
    banners: List<Banner>,
    navController: NavController,
    viewModel: ShoppingAppViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var nextIndex by remember {
        mutableStateOf(banners.size - 1)
    }
    LaunchedEffect(Unit) {
        while (true) {
            coroutineScope.launch {
                // Calculate the next index, wrapping back to 0 when the end is reached
                if (nextIndex >= banners.size - 1) {
                    nextIndex = 0
                } else {
                    nextIndex++
                }
                println("INDEX $nextIndex, ${listState.firstVisibleItemIndex}, ${banners.size} \n")
                listState.animateScrollToItem(nextIndex)

            }
            delay(2000) // Adjust the delay as needed
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(banners) { index, item ->
            val random = Random(index)
            val red = random.nextInt(0, 100)
            val green = random.nextInt(0, 100)
            val blue = random.nextInt(0, 100)
            val randomColor = Color(red, green, blue)
            Box(modifier = Modifier.fillMaxWidth()) {
                BannerCard(randomColor = randomColor, banner = item, viewModel = viewModel)
            }
        }

    }
}


@Preview
@Composable
private fun PreviewAddBanner() {
    val navController = rememberNavController()
    AddBanner(
        modifier = Modifier,
        navController = navController,
        viewModel = hiltViewModel()
    )
}

@Composable
fun AddBanner(modifier: Modifier, navController: NavController, viewModel: ShoppingAppViewModel) {
    var title by remember {
        mutableStateOf("")
    }
    var desc by remember {
        mutableStateOf("")
    }

    var img by remember {
        mutableStateOf("")
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewModel.uploadMedia(uri, BANNER_IMG) {
                    img = it
                }
            }
        }
    Column(
        modifier = modifier
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


        Card(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 190.dp, max = 200.dp),
            colors = CardDefaults.cardColors(Color.LightGray),
            elevation = CardDefaults.elevatedCardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(), verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {


                Column(modifier = Modifier.fillMaxWidth(.5f)) {


                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        textStyle = MaterialTheme.typography.titleLarge,
                        singleLine = true,
                        label = {
                            Text(
                                text = "Title",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                        })
                    Spacer(modifier = Modifier.heightIn(2.dp))
                    OutlinedTextField(
                        value = desc,
                        onValueChange = {
                            desc = it
                        },
                        textStyle = MaterialTheme.typography.bodySmall,
                        singleLine = true,
                        label = {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    )
                    var showLoading by remember {
                        mutableStateOf(false)
                    }
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            viewModel.addBanner(Banner(title = title, desc = desc, img = img))

                            if (viewModel.bannerState.value.isLoading) {
                                showLoading = true
                            } else {
                                showLoading = false
                                if (viewModel.bannerState.value.error.isNotBlank()) {
                                    Toast.makeText(
                                        context,
                                        viewModel.bannerState.value.error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        viewModel.bannerState.value.data,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.getAllBanners()
                                }
                            }


                        },
                        enabled = (title.isNotEmpty() && desc.isNotEmpty() && img.isNotEmpty())
                    ) {
                        if (showLoading) {
                            CircularProgressIndicator()
                        }
                        Text(text = "Add Banner")
                    }


                }

                Box {

                    SubcomposeAsyncImage(
                        model = img, contentDescription = "Banner Image", modifier = Modifier
                            .padding(5.dp)
                            .size(200.dp)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(10.dp)
                            )
                            .clip(
                                RoundedCornerShape(10.dp)
                            )
                            .clickable {

                                galleryLauncher.launch("image/*")
                            }, contentScale = ContentScale.Crop, loading = {
                            Image(
                                painter = painterResource(id = R.drawable.app),
                                contentDescription = "banner img",
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(
                                        RoundedCornerShape(10.dp)
                                    )
                            )
                        })

                    if (viewModel.loading.collectAsStateWithLifecycle().value) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    if (img.isBlank()) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "image icon",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }


            }


        }
    }

}


@Composable
fun BannerShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 150.dp), colors = CardDefaults.cardColors(Color.LightGray)

    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(), verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {


            Column(modifier = Modifier.fillMaxWidth(.5f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )


            }
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )


        }
    }
}

@Composable
fun BannerCard(
    modifier: Modifier = Modifier,
    banner: Banner = Banner(title = "Title", desc = "Description", img = "Image"),
    randomColor: Color,
    viewModel: ShoppingAppViewModel
) {
    val index = remember {
        mutableStateOf(0)
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
//            .fillMaxWidth()
            .widthIn(300.dp, 320.dp)
            .heightIn(min = 180.dp, max = 200.dp),
        colors = CardDefaults.cardColors(randomColor),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { viewModel.deleteBanner(banner) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "deleteBanner")
            }
        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(), verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {


            Column(modifier = Modifier.fillMaxWidth(.5f)) {
                Text(
                    text = banner.title.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp, color = Color.LightGray
                )
                Text(
                    text = banner.desc.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp, color = Color.LightGray
                )

            }

            SubcomposeAsyncImage(
                model = banner.img, contentDescription = "Banner Image", modifier = Modifier
                    .size(200.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    ), contentScale = ContentScale.Crop, loading = {
                    Image(
                        painter = painterResource(id = R.drawable.app),
                        contentDescription = "banner img",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(
                                RoundedCornerShape(10.dp)
                            )
                    )
                })

        }
    }
}

