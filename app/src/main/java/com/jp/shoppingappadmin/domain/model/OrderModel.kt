package com.jp.shoppingappadmin.domain.model

data class OrderModel(
    val id: String = System.currentTimeMillis().toString(),
    val product: ProductModel? = null,
    val quantity: Int = 1,
    val isCanceled: Boolean = false,
)
data class OrderParentModel (
    val id : String = System.currentTimeMillis().toString(),
    val name :String = "",
    val contact :String = "",
    val email :String = "",
    val order : OrderModel?= null,
    val address: String = "",
    val desc: String = "",
    val totalPrice: String = "",
    val status: List<String> = emptyList(),

)

