package com.jp.shoppingappadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.jp.shoppingappadmin.presentation.navigation.AppNavigation
import com.jp.shoppingappadmin.presentation.utils.AddBanner
import com.jp.shoppingappadmin.ui.theme.ShoppingAppAdminTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ShoppingAppAdminTheme {
//
                Box(
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                        AppNavigation()



                }

            }
        }
    }


}

