package com.example.vendora.domain.usecase.order

import com.example.vendora.domain.model.order.OrderWrapper
import com.example.vendora.domain.repo_interfaces.OrdersRepository
import javax.inject.Inject

class CreateShopifyOrderUserCase @Inject constructor(
    private val repository: OrdersRepository
) {

    operator fun invoke(orderWrapper: OrderWrapper){
        repository.createOrder(orderWrapper)
    }
}