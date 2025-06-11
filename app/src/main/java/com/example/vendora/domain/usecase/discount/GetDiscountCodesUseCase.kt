package com.example.vendora.domain.usecase.discount

import com.example.vendora.domain.model.discount.DiscountCode
import com.example.vendora.domain.repo_interfaces.DiscountRepository
import com.example.vendora.utils.wrapper.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscountCodesUseCase @Inject constructor (private val repository: DiscountRepository) {
    operator fun invoke ():Flow<Result<List<DiscountCode>>> {
        return repository.fetchDiscountCodes()
    }
}