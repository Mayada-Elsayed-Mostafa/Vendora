package com.example.vendora.data.repo_implementation

import com.example.vendora.data.local.UserPreferences
import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.FavoritesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class FavoritesRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val userPreferences: UserPreferences
) : FavoritesRepository {

    override suspend fun addFavoriteProduct(product: Product) {
        val userId = userPreferences.getUserId() ?: throw Exception("User not logged in")

        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(product.id.toString())
            .set(product)
            .await()
    }

    override fun getFavoriteProducts(): Flow<List<Product>> = callbackFlow {
        val userId = runBlocking { userPreferences.getUserId() }

        if (userId == null) {
            trySend(emptyList())
            awaitClose {}
            return@callbackFlow
        }

        val ref = firestore.collection("users")
            .document(userId)
            .collection("favorites")

        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }

            val products = snapshot?.documents?.mapNotNull {
                it.toObject(Product::class.java)
            } ?: emptyList()

            trySend(products)
        }

        awaitClose { listener.remove() }
    }

    override suspend fun removeFavoriteProduct(productId: Long) {
        val userId = userPreferences.getUserId() ?: throw Exception("User not logged in")

        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(productId.toString())
            .delete()
            .await()
    }

}