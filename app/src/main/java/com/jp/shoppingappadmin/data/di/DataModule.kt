package com.jp.shoppingappadmin.data.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFirebaseFireStore():FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage():FirebaseStorage{
        return FirebaseStorage.getInstance()
    }
}