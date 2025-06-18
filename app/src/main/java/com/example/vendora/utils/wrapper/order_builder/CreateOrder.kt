package com.example.vendora.utils.wrapper.order_builder

import com.example.vendora.domain.model.order.DiscountCodeBuild
import com.example.vendora.domain.model.order.LineItemBuild
import com.example.vendora.domain.model.order.OrderBuild
import com.example.vendora.domain.model.order.OrderWrapper
import com.example.vendora.domain.model.order.ShippingAddressBuild

class CreateOrder: OrderBuilder{
    private var email: String = ""
    private var financialStatus: String = ""
    private var lineItems: List<LineItemBuild> = emptyList()
    private var discountCodes: List<DiscountCodeBuild> = emptyList()
    private var currency: String = ""
    private var address: ShippingAddressBuild = ShippingAddressBuild( "", "", "", "", "", "", "")

    override fun email(email: String): CreateOrder = apply {
        this.email = email
    }

    override fun financialStatus(finStatus: String): CreateOrder = apply {
        this.financialStatus = finStatus
    }

    override fun lineItems(items: List<LineItemBuild>): CreateOrder = apply {
        this.lineItems = items
    }

    override fun discountCodes(codes: List<DiscountCodeBuild>): CreateOrder = apply {
        this.discountCodes = codes
    }

    override fun currency(currency: String): CreateOrder = apply {
        this.currency = currency
    }

    override fun address(address: ShippingAddressBuild): CreateOrder = apply {
        this.address = address
    }

    fun build(): OrderWrapper {

        return OrderWrapper(
            order = OrderBuild(
                fulfillment_status = null,
                currency = currency,
                email = email,
                financial_status = financialStatus,
                discount_codes = discountCodes,
                line_items = lineItems,
                send_receipt = true,
                shipping_address = address
            )
        )
    }

    override fun toString(): String {
        return "CreateOrder(email='$email', financialStatus='$financialStatus', lineItems=$lineItems, discountCodes=$discountCodes, currency='$currency', address=$address)"
    }


}