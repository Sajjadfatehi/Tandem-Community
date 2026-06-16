package com.sajjadfatehi.tandemcommunity.domain.usecase

import androidx.paging.PagingData
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember
import com.sajjadfatehi.tandemcommunity.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommunityUseCase @Inject constructor(
    private val repository: CommunityRepository
) {
    operator fun invoke(): Flow<PagingData<CommunityMember>> =
        repository.getCommunityPagingData()
}

