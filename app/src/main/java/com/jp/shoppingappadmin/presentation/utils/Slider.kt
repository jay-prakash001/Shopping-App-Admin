//package com.jp.shoppingappadmin.presentation.utils
//
//import androidx.compose.animation.core.EaseInOut
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.animateIntAsState
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.jp.shoppingappadmin.domain.model.Banner
//import kotlinx.coroutines.delay
//import kotlin.random.Random
//
//@Composable
//fun BannerSlider(modifier: Modifier = Modifier) {
//
//
//    val list = mutableListOf<Int>(1, 2, 3, 4, 5, 6)
//    var index by remember {
//
//        mutableStateOf(0)
//    }
//    val rotateCardY by animateFloatAsState(
//        targetValue = if (index % 2 == 0) 10f else 20f,
//        animationSpec = tween(durationMillis = 1700, easing = EaseInOut),
//        label = ""
//    )
//    val size by animateIntAsState(
//        targetValue = if (index % 2 == 0) 15 else 10,
////        animationSpec = spring(
////            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
////        )
//        animationSpec = tween(durationMillis = 1700, easing = EaseInOut),
//
//        )
//    LaunchedEffect(Unit) {
//        while (true) {
//            println("index : $index")
//
//            delay(900L) // Delay for 700 milliseconds
//
//            if (index >= list.size - 1) {
//
//                index = 0 // Reset to the first item when reaching the end
//            } else {
//                index++ // Move to the next item
//            }
//
//        }
//    }
//    val random = Random(index)
//    val red = random.nextInt(0, 100)
//    val green = random.nextInt(0, 100)
//    val blue = random.nextInt(0, 100)
//    val randomColor = Color(red, green, blue)
//    Box(modifier = modifier.height(300.dp)) {
//
//        BannerCard(
//            modifier = Modifier
//                .padding(size.dp) ,
//            banner = Banner(title = list[index].toString()), randomColor
//        )
//    }
//}