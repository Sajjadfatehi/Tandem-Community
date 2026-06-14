package com.sajjadfatehi.tandemcommunity.presentation

sealed interface CommunityEffect {
    data class ShowError(val message: String) : CommunityEffect
}