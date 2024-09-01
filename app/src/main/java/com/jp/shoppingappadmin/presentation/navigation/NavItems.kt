package com.jp.shoppingappadmin.presentation.navigation

import kotlinx.serialization.Serializable



@Serializable
object AddProducts

@Serializable
data class SearchProducts(var name: String)

@Serializable
data class SearchProductsByCategory(var name: String)

@Serializable
object DashBoard

@Serializable
object Notification

@Serializable
object Category

@Serializable
object Order

