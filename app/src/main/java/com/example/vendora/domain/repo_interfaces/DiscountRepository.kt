package com.example.vendora.domain.repo_interfaces

import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface DiscountRepository {
     fun fetchDiscountCodes(): Flow<Result<List<DiscountCode>>>
}