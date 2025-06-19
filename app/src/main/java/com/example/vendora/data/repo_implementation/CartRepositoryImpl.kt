package com.example.vendora.data.repo_implementation

import com.apollographql.apollo.api.Optional
import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.CartLinesRemoveMutation
import com.example.vendora.CartLinesUpdateMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.data.local.LocalDataSource
import com.example.vendora.data.remote.RemoteDataSource
import com.example.vendora.domain.repo_interfaces.CartRepository
import com.example.vendora.type.CartLineInput
import com.example.vendora.type.CartLineUpdateInput
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor( private val remoteDataSource: RemoteDataSource,private val localDataSource: LocalDataSource):CartRepository {



    override fun createCart(): Flow<Result<CartCreateMutation.Cart>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.createCart()
            val cart = response.cartCreate?.cart
            if(cart !=null){

                println("cartID: ${cart.id}")
                saveCartId(cart.id)

                emit(Result.Success(cart))
            }else{
                emit(Result.Failure(Exception("Failed to Create Cart")))
            }
        }catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun addToCart(
        cartId: String,
        variantId: String,
        quantity: Int
    ): Flow<Result<CartLinesAddMutation.Cart>> = flow {
       emit(Result.Loading)
        try {
            val lines = listOf(
                CartLineInput(
                    merchandiseId = variantId,
                    quantity = Optional.present(quantity)
                )
            )

            val response = remoteDataSource.addToCart(cartId,lines)
            val cart = response.cartLinesAdd?.cart
            if (cart != null) {
                emit(Result.Success(cart))
            } else {
                emit(Result.Failure(Exception("Failed to add to cart")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun updateCartLine(
        cartId: String,
        lineId: String,
        quantity: Int
    ): Flow<Result<CartLinesUpdateMutation.Cart>> = flow {
        emit(Result.Loading)
        try {
            val lines = listOf(
                CartLineUpdateInput(
                    id = lineId,
                    quantity = Optional.present(quantity)
                )
            )
            val response = remoteDataSource.updateCartLines(cartId, lines)
            val cart = response.cartLinesUpdate?.cart
            if (cart != null) {
                emit(Result.Success(cart))
            } else {
                emit(Result.Failure(Exception("Failed to update cart")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun removeFromCart(
        cartId: String,
        lineId: String
    ): Flow<Result<CartLinesRemoveMutation.Cart>> = flow{
        emit(Result.Loading)
        try {
            val response = remoteDataSource.removeFromCart(cartId, listOf(lineId))
            val cart = response.cartLinesRemove?.cart
            if (cart != null) {
                emit(Result.Success(cart))
            } else {
                emit(Result.Failure(Exception("Failed to remove from cart")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun getCart(cartId: String): Flow<Result<GetCartQuery.Cart>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getCart(cartId)
            val cart = response.cart
            if (cart != null) {

                emit(Result.Success(cart))
            } else {
                emit(Result.Failure(Exception("Cart not found")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    override fun getCartId(): String {
        println("SavedCardID : ${localDataSource.getString("cart_Id","")}")
        return localDataSource.getString("cart_Id","")
    }

    override fun saveCartId(cartId: String) {
       localDataSource.putString("cart_Id",cartId)
    }



}