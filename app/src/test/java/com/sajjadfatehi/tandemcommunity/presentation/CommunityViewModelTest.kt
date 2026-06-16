package com.sajjadfatehi.tandemcommunity.presentation

import androidx.paging.PagingData
import com.sajjadfatehi.tandemcommunity.domain.usecase.GetCommunityUseCase
import com.sajjadfatehi.tandemcommunity.domain.usecase.ObserveLikedMemberIdsUseCase
import com.sajjadfatehi.tandemcommunity.domain.usecase.ToggleLikeUseCase
import com.sajjadfatehi.tandemcommunity.rule.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCommunityUseCase = mockk<GetCommunityUseCase>()
    private val observeLikedMemberIdsUseCase = mockk<ObserveLikedMemberIdsUseCase>()
    private val toggleLikeUseCase = mockk<ToggleLikeUseCase>()

    private lateinit var viewModel: CommunityViewModel

    private fun createViewModel() {
        viewModel = CommunityViewModel(
            getCommunityUseCase,
            observeLikedMemberIdsUseCase,
            toggleLikeUseCase
        )
    }

    @Test
    fun `toggleLike should call use case`() = runTest {
        every { getCommunityUseCase() } returns flowOf(PagingData.empty())
        every { observeLikedMemberIdsUseCase() } returns flowOf(emptyList())

        coEvery { toggleLikeUseCase(any(), any()) } just Runs

        createViewModel()

        viewModel.onAction(
            CommunityAction.MemberLikeClicked(
                memberId = 10,
                currentState = true
            )
        )

        advanceUntilIdle()

        coVerify(exactly = 1) {
            toggleLikeUseCase(10, true)
        }
    }

}