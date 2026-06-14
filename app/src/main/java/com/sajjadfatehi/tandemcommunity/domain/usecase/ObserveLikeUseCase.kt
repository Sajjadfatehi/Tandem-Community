package com.sajjadfatehi.tandemcommunity.domain.usecase

import com.sajjadfatehi.tandemcommunity.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLikeUseCase @Inject constructor(
    private val repository: CommunityRepository
) {
    operator fun invoke(memberId: Int): Flow<Boolean> =
        repository.getLikeState(memberId)
}
