package com.sajjadfatehi.tandemcommunity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember
import com.sajjadfatehi.tandemcommunity.domain.usecase.GetCommunityUseCase
import com.sajjadfatehi.tandemcommunity.domain.usecase.ToggleLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    getCommunityUseCase: GetCommunityUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase
) : ViewModel() {

    val communityMembers: Flow<PagingData<CommunityMember>> =
        getCommunityUseCase().cachedIn(viewModelScope)

    fun onAction(action: CommunityAction) {
        when (action) {
            is CommunityAction.MemberLikeClicked -> {
                viewModelScope.launch {
                    toggleLikeUseCase(action.memberId, action.currentState)
                }
            }
        }
    }
}
