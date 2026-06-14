package com.sajjadfatehi.tandemcommunity.domain.usecase

import com.sajjadfatehi.tandemcommunity.domain.repository.CommunityRepository
import javax.inject.Inject

class ToggleLikeUseCase @Inject constructor(
    private val repository: CommunityRepository
) {
    suspend operator fun invoke(memberId: Int, currentState: Boolean) =
        repository.toggleLike(memberId, currentState)
}
