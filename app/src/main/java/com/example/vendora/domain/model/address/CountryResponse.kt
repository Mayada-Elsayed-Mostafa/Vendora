package com.example.vendora.domain.model.address

data class CountryResponse( val country: Country)

data class Country(
    val id: Long,
    val name: String,
    val code: String,
    val provinces: List<Province> = emptyList()
)

data class Province(
    val id: Long,
    val name: String,
    val code: String
)
