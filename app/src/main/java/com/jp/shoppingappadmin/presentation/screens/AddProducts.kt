package com.jp.shoppingappadmin.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingappadmin.R
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.presentation.utils.AlphaBox
import com.jp.shoppingappadmin.presentation.utils.ProductsGrid
import com.jp.shoppingappadmin.presentation.utils.ShimmerEffectBox
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingappadmin.ui.theme.cyan

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun AddProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.getCategories()
    }
    LaunchedEffect(key1 = true) {
        viewModel.getProducts()
    }
    val state = viewModel.categoryState.collectAsStateWithLifecycle().value
    val categoryList = viewModel.categories.collectAsStateWithLifecycle().value
    val img = remember {
        mutableStateOf(listOf<String>())
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uri ->
            uri.forEach {
                viewModel.uploadMedia(it) { imgUrl ->
                    img.value += imgUrl
                }
            }


        }
    var name by remember {
        mutableStateOf("")
    }

    var price by remember {
        mutableStateOf("")
    }
    var desc by remember {
        mutableStateOf("")
    }
    var category by remember {
        mutableStateOf("Category")
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var isAdding by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {

            TopAppBar(title = {
                Text(
                    text = "Add Products",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }, actions = {
                val icon = if (isAdding) Icons.Default.Close else Icons.Default.AddCircle
                Icon(imageVector = icon, contentDescription = "action", modifier = Modifier.clickable {
                    isAdding = !isAdding
                })
            })
        }
    ) { innerPadding ->


        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {


            if (viewModel.showAlertDialog.collectAsStateWithLifecycle().value) {
                AlphaBox(modifier = Modifier.fillMaxSize()) {
                    AlertDialog(onDismissRequest = {
                        viewModel.updateAlertDialogState()
                    }, confirmButton = {
                        Button(onClick = {

                            img.value = emptyList()
                            name = ""
                            desc = ""
                            price = ""
                            category = ""
                            viewModel.updateAlertDialogState()
                        }) {
                            Text(text = "OK")
                        }
                    }, title = {
                        Text(
                            text = "Category Added Successfully",
                            style = MaterialTheme.typography.titleMedium
                        )

                    }, icon = {
                        Image(
                            painter = painterResource(id = R.drawable.success),
                            contentDescription = "success",
                            modifier = Modifier.size(200.dp)
                        )
                    })
                }
            }

            if (state.error.isNotBlank()) {
                Text(text = state.error)
            } else {


                AnimatedVisibility(visible = !isAdding) {
                    ProductsGrid(products = viewModel.products.collectAsStateWithLifecycle().value)
                }
                AnimatedVisibility(visible = isAdding) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = {
                            Text(text = "Name")
                        }, singleLine = true
                        )




                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = {
                            Text(text = "Description")
                        }, singleLine = true)


                        Spacer(modifier = Modifier.height(10.dp))

                        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {
                            isExpanded = !isExpanded
                        }) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                value = category,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                                }, singleLine = true
                            )
                            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = {
                                isExpanded = false
                            }) {
                                categoryList.forEach {
                                    DropdownMenuItem(text = {
                                        Text(text = it.name)
                                    }, onClick = {
                                        category = it.name
                                        isExpanded = false

                                    })

                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))


                        OutlinedTextField(
                            value = price,
                            onValueChange = {
                                price = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true

                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth(.8f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            items(img.value) {
                                SubcomposeAsyncImage(model = it,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(100.dp)
                                        .clip(
                                            RoundedCornerShape(10.dp)
                                        ),
                                    contentScale = ContentScale.Crop,
                                    loading = {
                                        ShimmerEffectBox(modifier = Modifier.size(100.dp))
                                    })
                            }
                            item {
                                Box(modifier = Modifier.size(100.dp)) {
                                    ShimmerEffectBox()
                                    IconButton(onClick = {
                                        galleryLauncher.launch("image/*")
                                    }, modifier = Modifier.align(Alignment.Center)) {

                                        if (viewModel.loading.collectAsStateWithLifecycle().value) {
                                            CircularProgressIndicator()
                                        }
                                        if (img.value.isEmpty()) {
                                            Icon(
                                                imageVector = Icons.Default.PermMedia,
                                                contentDescription = "pick images"
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "pick images"
                                            )
                                        }
                                    }


                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                val product = ProductModel(
                                    name = name,
                                    desc = desc,
                                    category = category,
                                    price = price.toLong(),
                                    images = img.value
                                )
                                viewModel.addProduct(product)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = cyan),
                            enabled = !viewModel.loading.collectAsStateWithLifecycle().value
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }

                }
            }
            if (state.isLoading) {
                AlphaBox(modifier = Modifier.fillMaxSize()) {

                    CircularProgressIndicator(
                        color = cyan,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }


        }
    }
}
