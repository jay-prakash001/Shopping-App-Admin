package com.jp.shoppingappadmin.domain.repo

import android.net.Uri
import com.jp.shoppingappadmin.common.IMAGES
import com.jp.shoppingappadmin.common.ResultState
import com.jp.shoppingappadmin.domain.model.Banner
import com.jp.shoppingappadmin.domain.model.CategoryModel
import com.jp.shoppingappadmin.domain.model.OrderModel
import com.jp.shoppingappadmin.domain.model.OrderParentModel
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ShoppingAppRepo {

    suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>>
    suspend fun getAllCategories(): Flow<ResultState<List<CategoryModel>>>
    suspend fun deleteCategory(category: CategoryModel): Flow<ResultState<String>>
    fun searchCategories(name: String): Flow<ResultState<List<CategoryModel>>>

    suspend fun addProduct(product: ProductModel): Flow<ResultState<String>>
    suspend fun getAllProducts(): Flow<ResultState<List<ProductModel>>>
    suspend fun deleteProduct(product: ProductModel): Flow<ResultState<String>>
    fun searchProducts(name: String): Flow<ResultState<List<ProductModel>>>
    fun searchProductsByCategoryName(category: String): Flow<ResultState<List<ProductModel>>>

    suspend fun uploadMedia(uri: Uri, path: String = IMAGES): Flow<ResultState<String>>


    suspend fun addBanner(banner: Banner): Flow<ResultState<String>>
    suspend fun deleteBanner(banner: Banner): Flow<ResultState<String>>
    suspend fun getBanners(): Flow<ResultState<List<Banner>>>

    suspend fun getUsers(): Flow<ResultState<List<User>>>

    suspend fun getOrders(email: String): Flow<ResultState<List<OrderParentModel>>>
    suspend fun updateOrder(
        email: String,
        orderParentModel: OrderParentModel,
        statusValue: String
    ): Flow<ResultState<List<OrderParentModel>>>


}