package com.sajjadfatehi.tandemcommunity.presentation

sealed interface CommunityAction {
    data class MemberLikeClicked(
        val memberId: Int,
        val currentState: Boolean
    ) : CommunityAction
}
