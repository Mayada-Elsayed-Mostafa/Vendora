package com.example.vendora.di

import android.content.Context
import com.example.vendora.data.local.UserPreferences
import com.example.vendora.data.repo_implementation.FavoritesRepositoryImpl
import com.example.vendora.domain.repo_interfaces.FavoritesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFavoritesRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FavoritesRepository = FavoritesRepositoryImpl(firestore, auth)

}
