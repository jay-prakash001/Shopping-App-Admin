package com.jp.shoppingappadmin.data.repo

import android.annotation.SuppressLint
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.jp.shoppingappadmin.common.BANNER_COLLECTION
import com.jp.shoppingappadmin.common.CATEGORY
import com.jp.shoppingappadmin.common.CATEGORY_COLLECTION
import com.jp.shoppingappadmin.common.ID
import com.jp.shoppingappadmin.common.IMAGES
import com.jp.shoppingappadmin.common.NAME
import com.jp.shoppingappadmin.common.PRODUCT_COLLECTION
import com.jp.shoppingappadmin.common.ResultState
import com.jp.shoppingappadmin.domain.model.Banner
import com.jp.shoppingappadmin.domain.model.CategoryModel
import com.jp.shoppingappadmin.domain.model.ProductModel
import com.jp.shoppingappadmin.domain.repo.ShoppingAppRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ShoppingAppRepoImpl @Inject constructor(
    val firestore: FirebaseFirestore,
    val firebaseStorage: FirebaseStorage
) : ShoppingAppRepo {
    override suspend fun addCategory(category: CategoryModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firestore.collection(
                CATEGORY_COLLECTION
            ).add(category).addOnFailureListener {
                trySend(ResultState.Error("failed"))
            }.addOnSuccessListener {
                trySend(ResultState.Success("success"))
            }
            awaitClose {
                close()
            }
        }

    override suspend fun deleteCategory(category: CategoryModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firestore.collection(CATEGORY_COLLECTION).whereEqualTo(ID, category.id).get()
                .addOnSuccessListener { querySnapShot ->
                    if (!querySnapShot.isEmpty) {
                        val document = querySnapShot.documents[0]
                        document.reference.delete().addOnSuccessListener {
                            trySend(ResultState.Success("Successfully deleted"))
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.localizedMessage))
                        }
                    } else {
                        trySend(ResultState.Error("Category Not Found."))
                    }
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))

                }
            awaitClose {
                close()
            }
        }

    override suspend fun getAllCategories(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firestore.collection(CATEGORY_COLLECTION).get().addOnSuccessListener { it: QuerySnapshot ->
            val categories = mutableListOf<CategoryModel>()
            it.documents.forEach {
                val category = it.toObject(CategoryModel::class.java)
                if (category != null) {
                    categories.add(category)
                }
            }
            trySend(ResultState.Success(categories))

        }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage))
        }
        awaitClose {
            close()
        }
    }

    override fun searchCategories(name: String): Flow<ResultState<List<CategoryModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firestore.collection(CATEGORY_COLLECTION).whereEqualTo(NAME, name).get()
                .addOnSuccessListener {
                    val categories = mutableListOf<CategoryModel>()
                    it.documents.forEach {
                        val document = it.toObject(CategoryModel::class.java)
                        if (document != null) {
                            categories.add(document)
                        }
                    }
                    trySend(ResultState.Success(categories))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }

    override suspend fun addBanner(banner: Banner): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(BANNER_COLLECTION).add(banner).addOnSuccessListener {
            trySend(ResultState.Success("Successfully added"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun deleteBanner(banner: Banner): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(BANNER_COLLECTION).whereEqualTo(ID, banner.id).get()
            .addOnSuccessListener { querySnapShot ->
                if (!querySnapShot.isEmpty) {
                    val document = querySnapShot.documents[0]
                    document.reference.delete().addOnSuccessListener {
                        trySend(ResultState.Success("Successfully deleted"))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.localizedMessage))

                    }
                } else {
                    trySend(ResultState.Error("Banner Not Found."))
                }

            }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage))
        }
        awaitClose { close() }
    }

    override suspend fun getBanners(): Flow<ResultState<List<Banner>>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(BANNER_COLLECTION).get().addOnSuccessListener { querySnapshot ->
            val banners = mutableListOf<Banner>()
            querySnapshot.documents.forEach { it ->
                val banner = it.toObject(Banner::class.java)

                if (banner != null) {
                    banners.add(banner)
                }
            }
            trySend(ResultState.Success(banners))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage))
        }
        awaitClose {
            close()
        }

    }


    override suspend fun addProduct(product: ProductModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firestore.collection(PRODUCT_COLLECTION).add(product).addOnFailureListener {
                trySend(ResultState.Error("failed"))
            }.addOnSuccessListener {
                trySend(ResultState.Success("success"))
            }
            awaitClose {
                close()
            }
        }

    override suspend fun getAllProducts(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(PRODUCT_COLLECTION).get().addOnSuccessListener { it: QuerySnapshot ->
            val products = mutableListOf<ProductModel>()
            it.documents.forEach {
                val product = it.toObject(ProductModel::class.java)
                if (product != null) {
                    products.add(product)
                }

            }
            trySend(ResultState.Success(products))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage!!.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun deleteProduct(product: ProductModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(PRODUCT_COLLECTION).whereEqualTo(ID, product.id).get().addOnSuccessListener {querySnapshot->
            if (!querySnapshot.isEmpty){
                val document = querySnapshot.documents[0]
                document.reference.delete().addOnSuccessListener {
                    trySend(ResultState.Success("Successfully deleted"))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.localizedMessage))
                    }
            }else {
                trySend(ResultState.Error("Product Not Found."))
            }
        }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage))
        }
        awaitClose {
            close()
        }
    }

    override fun searchProducts(name: String): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firestore.collection(PRODUCT_COLLECTION).whereEqualTo(NAME, name).get()
                .addOnSuccessListener { querySnapShot ->
                    val products = mutableListOf<ProductModel>()

                    querySnapShot.documents.forEach {
                        val document = it.toObject(ProductModel::class.java)
                        if (document != null) {
                            products.add(document)
                        }
                    }
                    trySend(ResultState.Success(products))

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }


    override fun searchProductsByCategoryName(category: String): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firestore.collection(PRODUCT_COLLECTION).whereEqualTo(CATEGORY, category).get()
                .addOnSuccessListener {
                    val products = mutableListOf<ProductModel>()
                    it.documents.forEach {
                        val document = it.toObject(ProductModel::class.java)
                        if (document != null) {
                            products.add(document)
                        }
                    }
                    trySend(ResultState.Success(products))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }


    override suspend fun uploadMedia(uri: Uri, path: String): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseStorage.reference.child(path).child(System.currentTimeMillis().toString())
                .putFile(uri).addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result.storage.downloadUrl.addOnSuccessListener {
                            trySend(ResultState.Success(it.toString()))
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.localizedMessage!!.toString()))
                        }
                    } else {
                        trySend(ResultState.Error(it.exception!!.localizedMessage!!.toString()))
                    }
                }
            awaitClose {
                close()
            }
        }


}