package com.example.vendora.ui.ui_model

import com.example.vendora.R

data class SubCategory (
    val name: String,
    val icon: Int
)

val subCategories = listOf(
    SubCategory(name = "T-SHIRTS", icon = R.drawable.t_shirt),
    SubCategory(name = "SHOES", icon = R.drawable.shoe),
    SubCategory(name = "ACCESSORIES", icon = R.drawable.necklace)
)