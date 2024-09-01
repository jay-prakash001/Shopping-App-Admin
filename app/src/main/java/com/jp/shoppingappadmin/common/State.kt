package com.jp.shoppingappadmin.common

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class  Error<T>(val exception: String) : ResultState<T>()
    data object Loading : ResultState<Nothing>()
}