package com.jp.shoppingappadmin.presentation.utils

import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.jp.shoppingappadmin.common.EMAIL
import com.jp.shoppingappadmin.common.USER_COLLECTION
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.runBlocking

fun hideNavigationBar(window : Window) {
    // Get the WindowInsetsController
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

    // Hide the navigation bar
    windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
}

fun getRef(email: String, firestore: FirebaseFirestore): String {

    var node = ""
    firestore.collection(USER_COLLECTION)
        .whereEqualTo(EMAIL, email)
        .get()
        .addOnSuccessListener { querySnapshot ->
            // Check if any documents were returned
            if (!querySnapshot.isEmpty) {

                node =  querySnapshot.documents[0].id + querySnapshot.documents[0].data


            }
        }
       return node

}
