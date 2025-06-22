package com.example.vendora.domain.usecase.cart

import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.CartLinesRemoveMutation
import com.example.vendora.CartLinesUpdateMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.domain.repo_interfaces.CartRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CreateCartUseCase @Inject constructor(private val repository: CartRepository){
    operator fun invoke(): Flow<Result<CartCreateMutation.Cart>> {
        return repository.createCart()
    }
}

class AddToCartUseCase  @Inject constructor(private val repository: CartRepository){
    operator fun invoke(cartId: String, variantId: String, quantity: Int): Flow<Result<CartLinesAddMutation.Cart>> {
        return repository.addToCart(cartId,variantId,quantity)
    }
}

class UpdateCartLineUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(cartId: String, lineId: String, quantity: Int): Flow<Result<CartLinesUpdateMutation.Cart>> {
        return repository.updateCartLine(cartId, lineId, quantity)
    }
}


class RemoveFromCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(cartId: String, lineId: String): Flow<Result<CartLinesRemoveMutation.Cart>> {
        return repository.removeFromCart(cartId, lineId)
    }
}


class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(cartId: String): Flow<Result<GetCartQuery.Cart>> {
        return repository.getCart(cartId)
    }
}


class RemoveAllLinesFromCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(cartId: String, linesId: List<String>): Flow<Result<CartLinesRemoveMutation.Cart>> {
        return repository.removeAllLinesFromCart(cartId, linesId)
    }
}