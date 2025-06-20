package com.example.vendora.domain.model.product

data class Product(
    val admin_graphql_api_id: String,
    val body_html: String,
    val created_at: String,
    val handle: String,
    val id: Long,
    val image: Image,
    val images: List<Image>,
    val options: List<Option>,
    val product_type: String,
    val published_at: String,
    val published_scope: String,
    val status: String,
    val tags: String,
    val template_suffix: Any,
    val title: String,
    val updated_at: String,
    val variants: List<Variant>,
    val vendor: String
)

fun Product.findVariantIdByColorAndSize(
    selectedColor: String? = null,
    selectedSize: String? = null
): String? {

    val colorOptionIndex = options.indexOfFirst { it.name.equals("color", ignoreCase = true) }
    val sizeOptionIndex = options.indexOfFirst { it.name.equals("size", ignoreCase = true) }

    return variants.firstOrNull { variant ->

        (selectedColor == null || colorOptionIndex == -1 || when (colorOptionIndex) {
            0 -> variant.option1 == selectedColor
            1 -> variant.option2 == selectedColor
            2 -> variant.option3.toString() == selectedColor
            else -> true
        }) &&
                (selectedSize == null || sizeOptionIndex == -1 || when (sizeOptionIndex) {
                    0 -> variant.option1 == selectedSize
                    1 -> variant.option2 == selectedSize
                    2 -> variant.option3.toString() == selectedSize
                    else -> true
                })
    }?.admin_graphql_api_id
}