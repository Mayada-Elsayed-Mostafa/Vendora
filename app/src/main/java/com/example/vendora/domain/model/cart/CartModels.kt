package com.example.vendora.domain.model.cart

data class CartLineInput (
    val merchandiseId: String,
    val quantity: Int
)
data class CartAttributeInput(
    val key: String,
    val value: String
)

data class CartCreateInput(
    val lines : List<CartLineInput>? = null,
    val attributes: List<CartAttributeInput>? = null
)

data class CartLineUpdateInput(
    val id: String,
    val quantity: Int
)

//////////////////////////////////////////////////////
data class Price(
    val amount: String,
    val currencyCode: String
)

data class CartLineCost(
    val totalAmount: Price
)

data class CartCost(
    val totalAmount: Price,
)

data class ProductImage(
    val url: String
)

data class ImageEdge(
    val node: ProductImage
)

data class ProductImages(
    val edges: List<ImageEdge>
)

data class CartProduct(
    val id: String,
    val title: String,
    val images: ProductImages
)

data class CartMerchandise(
    val id: String,
    val title: String,
    val price: Price,
    val product: CartProduct
)


data class CartLine1(
    val id: String,
    val quantity: Int,
    val merchandise: CartMerchandise,
    val cost: CartLineCost
)

data class CartLineEdge(
    val node: CartLine1
)

data class CartLines(
    val edges: List<CartLineEdge>
)




data class Cart(
    val id: String,
    val totalQuantity: Int,
    val lines: CartLines,
    val cost: CartCost
)