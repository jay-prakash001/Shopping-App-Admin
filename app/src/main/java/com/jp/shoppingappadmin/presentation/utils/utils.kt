package com.jp.shoppingappadmin.presentation.utils

import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

fun hideNavigationBar(window : Window) {
    // Get the WindowInsetsController
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

    // Hide the navigation bar
    windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
}