package com.jp.shoppingappadmin.domain.model

data class Banner(
    var id : String = System.currentTimeMillis().toString(),
    var title : String = "",
    var desc : String = "",
    var img : String = ""
)