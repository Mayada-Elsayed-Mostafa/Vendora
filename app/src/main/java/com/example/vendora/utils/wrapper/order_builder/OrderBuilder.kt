package com.example.vendora.utils.wrapper.order_builder

import com.example.vendora.domain.model.order.DiscountCodeBuild
import com.example.vendora.domain.model.order.LineItemBuild
import com.example.vendora.domain.model.order.ShippingAddressBuild

interface OrderBuilder {
    fun email(email: String): OrderBuilder

    fun financialStatus(finStatus: String): OrderBuilder

    fun lineItems(items: List<LineItemBuild>): OrderBuilder

    fun discountCodes(codes: List<DiscountCodeBuild>): OrderBuilder

    fun currency(currency: String): OrderBuilder

    fun address(address: ShippingAddressBuild): OrderBuilder
}