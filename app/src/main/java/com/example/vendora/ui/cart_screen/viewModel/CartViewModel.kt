package com.example.vendora.ui.cart_screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendora.CartCreateMutation
import com.example.vendora.CartLinesAddMutation
import com.example.vendora.CartLinesRemoveMutation
import com.example.vendora.CartLinesUpdateMutation
import com.example.vendora.GetCartQuery
import com.example.vendora.domain.repo_interfaces.CartRepository
import com.example.vendora.domain.usecase.cart.AddToCartUseCase
import com.example.vendora.domain.usecase.cart.CreateCartUseCase
import com.example.vendora.domain.usecase.cart.GetCartUseCase
import com.example.vendora.domain.usecase.cart.RemoveAllLinesFromCartUseCase
import com.example.vendora.domain.usecase.cart.RemoveFromCartUseCase
import com.example.vendora.domain.usecase.cart.UpdateCartLineUseCase
import com.example.vendora.utils.wrapper.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val createCartUseCase: CreateCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartLineUseCase: UpdateCartLineUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val repository: CartRepository,
    private val removeAllLinesFromCartUseCase: RemoveAllLinesFromCartUseCase,
    private val auth:FirebaseAuth
):ViewModel(){

    private var _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow()

    private var _cartItems = MutableStateFlow<Result<GetCartQuery.Cart>>(Result.Loading)
    val cartItems = _cartItems.asStateFlow()

   // private val auth = FirebaseAuth.getInstance()

    init {
        _uiState.update { it.copy(cartId = repository.getCartId()) }

        //_uiState.value.cartId?.let { loadCart(it) }

    }



    fun checkOrCreateCart(){
        val email = auth.currentUser?.email
        if (email != null){
            val existingCardId = repository.getString(email,"")
            println("existingCardId : $existingCardId")
            if (existingCardId.isNotEmpty()){
                _uiState.update { it.copy(cartId = existingCardId) }
                loadCart(existingCardId)
            }else{
                println("email : $email")
                createCartAndSave(email)
            }
        }else{
            _uiState.update { it.copy(errorMessage = "User not logged in") }
            println(_uiState.value.errorMessage)
        }
    }

    private fun createCartAndSave(email: String){
        viewModelScope.launch {
            createCartUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    when(result){
                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    createCartResult = result,
                                    errorMessage = result.exception.message ?: "Failed to create cart"
                                )
                            }
                        }
                       is Result.Loading -> {
                           _uiState.update { it.copy(createCartResult = result) }
                       }
                        is Result.Success -> {
                            val cardId = result.data.id
                            repository.saveString(email,cardId)

                            _uiState.update {
                                it.copy(
                                    createCartResult = result,
                                    cartId = result.data.id
                                )
                            }

                            loadCart(result.data.id)

                        }
                    }

                }
        }
    }

    fun createCart(){
        viewModelScope.launch {
            createCartUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    when(result){
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    createCartResult = result,
                                    cartId = result.data.id,
                                    totalQuantity = result.data.totalQuantity ,
                                    totalAmount = result.data.cost.totalAmount.amount.toString() ,
                                    currencyCode =  "EGP",
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }
                        }
                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    createCartResult = result,
                                    isLoading = false,
                                    errorMessage = result.exception.message ?: "Failed to create cart"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }

                }
        }
    }


    fun addToCart(variantId:String,quantity: Int = 1){


        val cartId = repository.getString(auth.currentUser?.email?:"","")


        println("addToCart Id :$cartId")

        _uiState.update {
            it.copy(
                isAddingToCart = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            addToCartUseCase.invoke(cartId,variantId,quantity)
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    when(result){
                        is Result.Success ->{
                            _uiState.update {
                                it.copy(
                                    addToCartResult = result,
                                    isAddingToCart = false,
                                    totalQuantity = result.data.totalQuantity ,
                                    totalAmount = result.data.cost.totalAmount.amount.toString() ,
                                    currencyCode = result.data.cost.totalAmount.currencyCode.toString(),
                                    errorMessage = null
                                )
                            }
                            println("$$$$$$$$$$$$"+_uiState.value.addToCartResult)

                        }

                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    addToCartResult = result,
                                    isAddingToCart = false,
                                    errorMessage = result.exception.message ?: "Failed to add item to cart"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update { it.copy(addToCartResult = result) }
                        }
                    }


                }
        }
    }


    private fun createCartAndAddProduct(variantId: String, quantity: Int) {
        viewModelScope.launch {
            createCartUseCase.invoke()
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    createCartResult = result,
                                    cartId = result.data.id
                                )
                            }
                            addToCart(variantId, quantity)
                        }
                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    createCartResult = result,
                                    isAddingToCart = false,
                                    errorMessage = result.exception.message ?: "Failed to create cart"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update { it.copy(createCartResult = result) }
                        }
                    }
                }
        }
    }


    fun updateCartLineQuantity(lineId: String, quantity: Int) {
        val cartId = _uiState.value.cartId ?: return

        _uiState.update {
            it.copy(
                isUpdatingCart = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            updateCartLineUseCase.invoke(cartId, lineId, quantity)
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    updateCartResult = result,
                                    isUpdatingCart = false,
                                    totalQuantity = result.data.totalQuantity ,
                                    totalAmount = result.data.cost.totalAmount.amount.toString() ,
                                    currencyCode = result.data.cost.totalAmount.currencyCode.toString() ?: "EGP",
                                    errorMessage = null
                                )
                            }
                            //loadCart(cartId)
                        }
                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    updateCartResult = result,
                                    isUpdatingCart = false,
                                    errorMessage = result.exception.message ?: "Failed to update cart"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update { it.copy(updateCartResult = result) }
                        }
                    }
                }
        }
    }


    fun removeFromCart(lineId: String) {
        val cartId = _uiState.value.cartId ?: return

        _uiState.update {
            it.copy(
                isUpdatingCart = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            removeFromCartUseCase.invoke(cartId, lineId)
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    removeFromCartResult = result,
                                    isUpdatingCart = false,
                                    totalQuantity = result.data.totalQuantity ,
                                    totalAmount = result.data.cost.totalAmount.amount.toString() ,
                                    currencyCode = result.data.cost.totalAmount.currencyCode.toString(),
                                    errorMessage = null
                                )
                            }
                            loadCart(cartId)
                        }
                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    removeFromCartResult = result,
                                    isUpdatingCart = false,
                                    errorMessage = result.exception.message ?: "Failed to remove item"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update { it.copy(removeFromCartResult = result) }
                        }
                    }
                }
        }
    }

    fun loadCart(cartId: String) {
        _uiState.update {
            it.copy(
                cartId = cartId,
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            getCartUseCase.invoke(cartId)
                .flowOn(Dispatchers.IO)
                .collect { result ->

                    when (result) {

                        is Result.Success -> {

                            _uiState.update {
                                it.copy(
                                    loadCartResult = result,
                                    totalQuantity = result.data.totalQuantity ,
                                    totalAmount = result.data.cost.totalAmount.amount.toString(),
                                    currencyCode = result.data.cost.totalAmount.currencyCode.toString() ,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }
                            _cartItems.value = Result.Success(result.data)

                        }
                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    loadCartResult = result,
                                    isLoading = false,
                                    errorMessage = result.exception.message ?: "Failed to load cart"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    loadCartResult = result,
                                    isLoading = true
                                )
                            }
                        }
                    }
                }
        }
    }

    fun clearCart() {
        val cartId = _uiState.value.cartId ?: return

        val currentCart = cartItems.value
        if (currentCart !is Result.Success) return

        val lineIds = currentCart.data.lines.edges.map { it.node.id }

        if (lineIds.isEmpty()) return

        _uiState.update { it.copy(isUpdatingCart = true, errorMessage = null) }

        viewModelScope.launch {
            removeAllLinesFromCartUseCase(cartId, lineIds)
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    removeFromCartResult = result,
                                    isUpdatingCart = false,
                                    totalQuantity = result.data.totalQuantity,
                                    totalAmount = result.data.cost.totalAmount.amount.toString(),
                                    currencyCode = result.data.cost.totalAmount.currencyCode.toString(),
                                    errorMessage = null
                                )
                            }
                            loadCart(cartId)
                        }
                        is Result.Failure -> {
                            _uiState.update {
                                it.copy(
                                    removeFromCartResult = result,
                                    isUpdatingCart = false,
                                    errorMessage = result.exception.message ?: "Failed to clear cart"
                                )
                            }
                        }
                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(removeFromCartResult = result)
                            }
                        }
                    }
                }
        }
    }

    fun setCartIdForTest(cartId: String) {
        _uiState.update { it.copy(cartId = cartId) }
    }

}


data class CartUiState(
    val loadCartResult:  Result<GetCartQuery.Cart> = Result.Loading,
    val addToCartResult:  Result<CartLinesAddMutation.Cart>? = null,
    val createCartResult:  Result<CartCreateMutation.Cart>? = null,
    val updateCartResult:  Result<CartLinesUpdateMutation.Cart>? = null,
    val removeFromCartResult:  Result<CartLinesRemoveMutation.Cart>? = null,
    val cartId: String? = null,
    val isAddingToCart: Boolean = false,
    val isUpdatingCart: Boolean = false,
    val totalQuantity: Int = 0,
    val totalAmount: String = "0.00",
    val currencyCode: String = "EGP",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)