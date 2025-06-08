package com.example.vendora.ui.ui_model

import com.example.vendora.R

data class GiftCardAd(
    val couponAmount: Int,
    val couponTitle: String,
    val couponDesc: String,
    val couponImage: Int
)

val couponList = listOf(
    GiftCardAd(
        couponAmount = 15,
        couponTitle = "Today's Special",
        couponDesc = "Get Discount for Every order, only for today",
        couponImage = R.drawable.backbag
    ),
    GiftCardAd(
        couponAmount = 10,
        couponTitle = "Weekend Deals",
        couponDesc = "Enjoy with your weekend sale",
        couponImage = R.drawable.shoes
    )
)

