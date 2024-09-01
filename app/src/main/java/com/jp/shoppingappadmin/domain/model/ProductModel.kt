package com.jp.shoppingappadmin.domain.model

data class ProductModel(
    var id: String = System.currentTimeMillis().toString(),
    var name: String = "",
    var date: Long = System.currentTimeMillis(),
    var category: String = "",
    var price: Long = 0,
    var desc: String = "",
    var images: List<String> = emptyList()
)