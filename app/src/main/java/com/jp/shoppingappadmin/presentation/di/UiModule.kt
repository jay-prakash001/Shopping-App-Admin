package com.jp.shoppingappadmin.presentation.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jp.shoppingappadmin.data.repo.ShoppingAppRepoImpl
import com.jp.shoppingappadmin.domain.repo.ShoppingAppRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UiModule {

    @Provides
    fun provideRepo(
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): ShoppingAppRepo {
        return ShoppingAppRepoImpl(firestore, firebaseStorage)
    }
}