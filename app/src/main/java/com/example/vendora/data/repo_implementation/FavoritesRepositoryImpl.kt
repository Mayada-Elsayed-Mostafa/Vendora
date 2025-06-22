package com.example.vendora.data.repo_implementation

import com.example.vendora.domain.model.product.Product
import com.example.vendora.domain.repo_interfaces.FavoritesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FavoritesRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FavoritesRepository {

    override suspend fun addFavoriteProduct(product: Product) {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")

        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(product.id.toString())
            .set(product)
            .await()
    }

    override fun getFavoriteProducts(): Flow<List<Product>> = callbackFlow {
        val userId = auth.currentUser?.uid ?: run {
            close(Exception("User not logged in"))
            return@callbackFlow
        }

        val ref = firestore.collection("users")
            .document(userId)
            .collection("favorites")

        val listener = ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
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
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")

        firestore.collection("users")
            .document(userId)
            .collection("favorites")
            .document(productId.toString())
            .delete()
            .await()
    }

}