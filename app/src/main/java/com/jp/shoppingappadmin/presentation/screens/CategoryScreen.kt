package com.jp.shoppingappadmin.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingappadmin.R
import com.jp.shoppingappadmin.common.CATEGORY_IMG
import com.jp.shoppingappadmin.presentation.utils.AlphaBox
import com.jp.shoppingappadmin.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingappadmin.ui.theme.cyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavController
) {

    val isAdding = remember {
        mutableStateOf(false)
    }
    val state = viewModel.categoryState.collectAsState().value
    LaunchedEffect(key1 = true) {
        viewModel.getCategories()
    }
    if (state.isLoading) {
        AlphaBox(modifier = Modifier.fillMaxSize()) {

            CircularProgressIndicator(color = cyan, modifier = Modifier)

        }
    } else if (state.error.isNotBlank()) {
        Text(text = state.error)
    } else {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Category",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                }, actions = {
                    IconButton(onClick = { isAdding.value = !isAdding.value }) {
                        val icon = if (isAdding.value) {
                            Icons.Default.Close
                        } else {
                            Icons.Default.Add
                        }
                        Icon(imageVector = icon, contentDescription = "")
                    }
                })
            }
        ) { innerPadding ->


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = !isAdding.value) {

                    AllCategories(navController = navController)
                }


                AnimatedVisibility(isAdding.value) {
                    AddCategory(viewModel)
                }

            }
        }
    }
}

@Composable
private fun AddCategory(viewModel: ShoppingAppViewModel) {
    val img = remember {
        mutableStateOf("")
    }
    val galleryLauncher =

        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewModel.uploadMedia(uri, CATEGORY_IMG) {
                    img.value = it
                }
            }

        }
    var name by remember {
        mutableStateOf("")
    }
    var createdBy by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Column( modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .height(300.dp)
            .clickable {
                galleryLauncher.launch("image/*")
            }
            .border(2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(10.dp))
            .clip(
                RoundedCornerShape(10.dp)
            )

    ) {

        if (viewModel.showAlertDialog.collectAsStateWithLifecycle().value) {
            AlphaBox(modifier = Modifier.fillMaxSize()) {

                AlertDialog(onDismissRequest = {
                    viewModel.updateAlertDialogState()
                }, confirmButton = {
                    Button(onClick = {
                        img.value = ""
                        name = ""
                        createdBy = ""
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
        if (viewModel.loading.collectAsStateWithLifecycle().value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        if (img.value.isNotBlank()) {
            SubcomposeAsyncImage(
                model = img.value,
                contentDescription = "selected Image",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop, loading = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.Center),
                            color = cyan,
                            trackColor = cyan.copy(.5f)
                        )

                    }
                }
            )
        } else {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "select Image",
                modifier = Modifier.align(Alignment.Center)
            )

        }
    }

    OutlinedTextField(
        value = name,
        onValueChange = {
            name = it
        }, label = {

            Text(text = "Category Name")
        })
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = createdBy,
        onValueChange = {
            createdBy = it
        }, label = {

            Text(text = "Created By")
        })

    Spacer(modifier = Modifier.height(10.dp))


    Button(
        onClick = {
            viewModel.addCategory(name, createdBy, img.value)
        },
        enabled = !viewModel.loading.collectAsStateWithLifecycle().value,
        colors = ButtonDefaults.buttonColors(containerColor = cyan)
    ) {
        Text(text = "Submit")
    }

    }
}