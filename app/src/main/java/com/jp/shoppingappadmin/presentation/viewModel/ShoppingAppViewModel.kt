package com.jp.shoppingappadmin.presentation.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jp.shoppingappadmin.common.IMAGES
import com.jp.shoppingappadmin.common.ResultState
import com.jp.shoppingappadmin.domain.model.Banner
import com.jp.shoppingappadmin.domain.model.CategoryModel
import com.jp.shoppingappadmin.domain.model.OrderParentModel
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.domain.model.User
import com.jp.shoppingappadmin.domain.repo.ShoppingAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingAppViewModel @Inject constructor(private val shoppingAppRepo: ShoppingAppRepo) :
    ViewModel() {
    private val _categoryState = MutableStateFlow(CategoryState())
    val categoryState = _categoryState.asStateFlow()
    var name: MutableStateFlow<String> = MutableStateFlow("")
    val categories = MutableStateFlow(emptyList<CategoryModel>())
    val products = MutableStateFlow(emptyList<ProductModel>())
    val loading = MutableStateFlow(false)
    private val _productState = MutableStateFlow(ProductState())
    val productState = _productState.asStateFlow()

    private val _showAlertDialog = MutableStateFlow(false)
    val showAlertDialog = _showAlertDialog.asStateFlow()

    val banners = MutableStateFlow(emptyList<Banner>())
    private val _bannerState = MutableStateFlow<BannerState>(BannerState())
    val bannerState = _bannerState.asStateFlow()
    private val _userState = MutableStateFlow(UserState())

    val userState = _userState.asStateFlow()
    val users = MutableStateFlow<List<User>>(emptyList())

    private val _orderState = MutableStateFlow(State())

    val orderState = _orderState.asStateFlow()
    val orders = MutableStateFlow<List<OrderParentModel>>(emptyList())


    init {
        getCategories()
        getUsers()
    }


    fun getUsers() {
        viewModelScope.launch {
            shoppingAppRepo.getUsers().collectLatest {
                when (it) {
                    is ResultState.Error -> {
                    _userState.value = UserState(error = it.exception)

                    }

                    ResultState.Loading -> {
                        _userState.value = UserState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _userState.value =UserState(data =  it.data.toString())
                        users.value = it.data
                    }
                }
            }
        }
    }

    fun getOrders(email : String) {
        viewModelScope.launch {
            shoppingAppRepo.getOrders(email = email).collectLatest {
                when(it){
                    is ResultState.Error -> {
                        _orderState.value = State(error = it.exception)
                    }
                    ResultState.Loading -> {

                        _orderState.value = State(isLoading =  true)
                    }
                    is ResultState.Success -> {

                        _orderState.value = State(data = it.data.toString())
                        orders.value = it.data
                    }
                }
            }
        }
    }

    fun updateOrder(email: String,orderParentModel: OrderParentModel,statusValue : String){
        viewModelScope.launch {
            shoppingAppRepo.updateOrder(email = email, orderParentModel = orderParentModel, statusValue = statusValue).collectLatest {
                when(it){
                    is ResultState.Error -> {


                    }
                    ResultState.Loading -> {

                    }
                    is ResultState.Success -> {
                        getOrders(email = email)
                    }
                }
            }
        }

    }
    fun updateAlertDialogState() {
        _showAlertDialog.value = !_showAlertDialog.value
    }

    fun addCategory(name: String, createdBy: String, img: String) {
        viewModelScope.launch {
            val category: MutableStateFlow<CategoryModel> = MutableStateFlow(
                CategoryModel(
                    name = name,
                    date = System.currentTimeMillis(),
                    createdBy = createdBy,
                    img = img
                )
            )

            println("category " + category.value.toString())

            shoppingAppRepo.addCategory(category.value).collectLatest { it ->
                println("category $it")
                when (it) {
                    is ResultState.Error -> {
                        _categoryState.value = CategoryState(error = it.exception)

                    }

                    ResultState.Loading -> {
                        _categoryState.value = CategoryState(isLoading = true)

                    }

                    is ResultState.Success -> {
                        getCategories()

                        _categoryState.value = CategoryState(data = it.data)
                        _showAlertDialog.value = true


                    }
                }

            }


        }
    }

    fun getCategories() {
        viewModelScope.launch {
            shoppingAppRepo.getAllCategories().collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        _categoryState.value = CategoryState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _categoryState.value = CategoryState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _categoryState.value = CategoryState(data = it.data.toString())
                        categories.value = it.data
                    }
                }

                println("CAT $it")

            }
        }
    }

    fun addProduct(productModel: ProductModel) {
        viewModelScope.launch {
            shoppingAppRepo.addProduct(productModel).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        _productState.value = ProductState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _productState.value = ProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productState.value = ProductState(data = it.data.toString())
                        _showAlertDialog.value = true
                    }
                }
            }

        }
    }

    fun getProducts() {
        viewModelScope.launch {
            shoppingAppRepo.getAllProducts().collectLatest {
                when (it) {

                    is ResultState.Error -> {
                        _productState.value = ProductState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _productState.value = ProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productState.value = ProductState(data = it.data.toString())
                        products.value = it.data
                    }
                }
            }
        }

    }

    fun uploadMedia(uri: Uri, path: String = IMAGES, onUploadSuccess: (String) -> Unit) {

        viewModelScope.launch {
            shoppingAppRepo.uploadMedia(uri, path).collectLatest {
                when (it) {
                    is ResultState.Success -> {
                        onUploadSuccess(it.data)
                        loading.value = false

                    }

                    is ResultState.Error -> {

                        loading.value = false
                    }

                    ResultState.Loading -> {

                        loading.value = true
                    }
                }


            }
        }
    }

    fun deleteCategory(categoryModel: CategoryModel): String {
        var res = ""
        viewModelScope.launch {
            shoppingAppRepo.deleteCategory(categoryModel).collectLatest {
                res = when (it) {
                    is ResultState.Error -> {
                        it.exception
                    }

                    ResultState.Loading -> {
                        "Loading"
                    }

                    is ResultState.Success -> {

                        it.data.toString()
                    }
                }

            }
        }
        return res
    }

    fun getAllBanners() {
        viewModelScope.launch {
            shoppingAppRepo.getBanners().collectLatest {
                println("BANNERS ${it}")

                when (it) {
                    is ResultState.Error -> {
                        _bannerState.value = BannerState(error = it.exception)
                    }

                    ResultState.Loading -> {

                        _bannerState.value = BannerState(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _bannerState.value = BannerState(data = "it.data")
                        banners.value = it.data
                    }
                }
            }
        }
    }

    fun addBanner(banner: Banner) {
        viewModelScope.launch {
            shoppingAppRepo.addBanner(banner).collectLatest {
                println("BANNERS ${it}")

                when (it) {
                    is ResultState.Error -> {
                        _bannerState.value = BannerState(error = it.exception)
                    }

                    ResultState.Loading -> {

                        _bannerState.value = BannerState(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _bannerState.value = BannerState(data = "it.data")


                    }
                }

            }

        }
    }

    fun deleteBanner(banner: Banner) {
        viewModelScope.launch {
            shoppingAppRepo.deleteBanner(banner).collectLatest {
                println("BANNERS ${it}")

                when (it) {
                    is ResultState.Error -> {
                        _bannerState.value = BannerState(error = it.exception)
                    }

                    ResultState.Loading -> {

                        _bannerState.value = BannerState(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _bannerState.value = BannerState(data = "it.data")
                        getAllBanners()

                    }
                }

            }

        }
    }

    fun deleteProduct(product: ProductModel) {
        viewModelScope.launch {
            shoppingAppRepo.deleteProduct(product).collectLatest {
                println("BANNERS ${it}")

                when (it) {
                    is ResultState.Error -> {
                        _productState.value = ProductState(error = it.exception)
                    }

                    ResultState.Loading -> {

                        _productState.value = ProductState(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _productState.value = ProductState(data = "it.data")
                        getProducts()

                    }
                }

            }

        }
    }


    fun searchProducts(name: String) {
        viewModelScope.launch {
            shoppingAppRepo.searchProducts(name).collectLatest {
                when (it) {

                    is ResultState.Error -> {
                        _productState.value = ProductState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _productState.value = ProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productState.value = ProductState(data = it.data.toString())
                        products.value = it.data
                    }
                }
            }
        }
    }

    fun searchCategories(name: String) {
        viewModelScope.launch {
            shoppingAppRepo.searchCategories(name).collectLatest {
                when (it) {

                    is ResultState.Error -> {
                        _productState.value = ProductState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _productState.value = ProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productState.value = ProductState(data = it.data.toString())
                        categories.value = it.data
                    }
                }
            }
        }
    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            shoppingAppRepo.searchProductsByCategoryName(category).collectLatest {
                when (it) {

                    is ResultState.Error -> {
                        _productState.value = ProductState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _productState.value = ProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productState.value = ProductState(data = it.data.toString())
                        products.value = it.data
                    }
                }
            }
        }
    }
}

data class CategoryState(
    var data: String = "",
    var isLoading: Boolean = false,
    var error: String = ""

)

data class ProductState(
    var data: String = "",
    var isLoading: Boolean = false,
    var error: String = ""

)

data class BannerState(
    var data: String = "",
    var isLoading: Boolean = false,
    var error: String = ""

)


data class UserState(
    var data: String = "",
    var isLoading: Boolean = false,
    var error: String = ""

)
data class State(
    var data: String = "",
    var isLoading: Boolean = false,
    var error: String = ""

)


