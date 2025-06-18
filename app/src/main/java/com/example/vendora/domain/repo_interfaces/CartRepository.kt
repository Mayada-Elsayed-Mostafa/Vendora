package com.example.vendora.domain.repo_interfaces

import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.CartLinesRemoveMutation
import com.example.vendora.CartLinesUpdateMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun createCart(): Flow<Result<CartCreateMutation.Cart>>

    fun addToCart(cartId:String, variantId:String,quantity:Int): Flow<Result<CartLinesAddMutation.Cart>>

    fun updateCartLine(cartId: String, lineId: String, quantity: Int) : Flow<Result<CartLinesUpdateMutation.Cart>>

    fun removeFromCart(cartId: String,lineId: String): Flow<Result<CartLinesRemoveMutation.Cart>>

    fun getCart(cartId: String): Flow<Result<GetCartQuery.Cart>>



    fun getCartId():String

    fun saveCartId(cartId: String)
}