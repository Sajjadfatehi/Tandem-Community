package com.sajjadfatehi.tandemcommunity.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.sajjadfatehi.tandemcommunity.R
import com.sajjadfatehi.tandemcommunity.domain.model.CommunityMember

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    CommunityScreen(
        modifier = modifier,
        communityMembers = viewModel.communityMembers.collectAsLazyPagingItems(),
        onAction = viewModel::onAction
    )
}

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    communityMembers: LazyPagingItems<CommunityMember>,
    onAction: (CommunityAction) -> Unit
) {

    val refreshState = communityMembers.loadState.refresh

    Box(modifier = Modifier.fillMaxSize()) {

        when (refreshState) {

            is LoadState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is LoadState.Error -> {
                ErrorView {
                    communityMembers.retry()
                }
            }

            else -> {
                if (communityMembers.itemCount == 0) {
                    EmptyResult()
                } else {
                    CommunityMembersList(modifier, communityMembers, onAction)
                }
            }
        }
    }

}

@Composable
private fun CommunityMembersList(
    modifier: Modifier,
    communityMembers: LazyPagingItems<CommunityMember>,
    onAction: (CommunityAction) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            count = communityMembers.itemCount,
            key = communityMembers.itemKey { it.id }
        ) { index ->
            val member = communityMembers[index] ?: return@items
            CommunityMemberItem(
                member = member,
                onLikeClick = {
                    onAction(
                        CommunityAction.MemberLikeClicked(
                            memberId = member.id,
                            currentState = member.isLiked
                        )
                    )
                }
            )
        }
        item {
            LoadStateFooter(loadState = communityMembers.loadState.append) {
                communityMembers.retry()
            }
        }
    }
}

@Composable
private fun CommunityMemberItem(
    modifier: Modifier = Modifier,
    member: CommunityMember,
    onLikeClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !member.isLiked, onClick = onLikeClick)
            .padding(
                start = 8.dp,
                top = 16.dp,
                end = 8.dp,
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(member)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MemberName(member)
                    Spacer(modifier = Modifier.weight(1f))
                    MemberReferenceCount(member)
                }
                Description(member)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LanguagesInfo(member)
                    IconToggleButton(
                        modifier = Modifier.padding(start = 16.dp),
                        checked = member.isLiked,
                        onCheckedChange = { onLikeClick() }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (member.isLiked) {
                                    R.drawable.ic_thumb_liked
                                } else {
                                    R.drawable.ic_thumb_not_liked
                                }
                            ),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun RowScope.LanguagesInfo(member: CommunityMember) {
    LanguageLabel(stringResource(R.string.native_label), member.natives.joinToString())
    LanguageLabel(stringResource(R.string.learns_label), member.learns.joinToString())
}

@Composable
private fun RowScope.LanguageLabel(label: String, value: String) {
    Text(text = label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
    Text(
        modifier = Modifier
            .weight(1f)
            .basicMarquee(iterations = Int.MAX_VALUE, velocity = 30.dp, initialDelayMillis = 0),
        text = value,
        maxLines = 1,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}


@Composable
private fun Description(member: CommunityMember) {
    Text(
        text = member.topic,
        maxLines = 2,
        style = MaterialTheme.typography.bodyMedium,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.outline,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun MemberReferenceCount(member: CommunityMember) {
    if (member.isNew) {
        Text(
            text = stringResource(R.string.badge_new),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 2.dp,
                    bottom = 2.dp
                ),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.surface
        )
    } else {
        Text(
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                top = 2.dp,
                bottom = 2.dp
            ),
            text = member.referenceCnt.toString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun MemberName(member: CommunityMember) {
    Text(
        text = member.firstName,
        maxLines = 1,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ProfileImage(member: CommunityMember) {
    Box(
        modifier = Modifier
            .padding(start = 4.dp)
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = member.pictureUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
private fun ErrorView(modifier: Modifier = Modifier, onRetryButtonClicked: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(320.dp)
                .padding(32.dp),
            painter = painterResource(R.drawable.illus_server_error),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(24.dp),
            text = stringResource(R.string.server_error_text),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error,
        )

        Button(
            modifier = Modifier.padding(24.dp),
            onClick = onRetryButtonClicked,
        ) {
            Text(
                text = stringResource(R.string.try_again)
            )
        }
    }
}

@Composable
private fun EmptyResult(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(320.dp)
                .padding(32.dp),
            painter = painterResource(R.drawable.illus_empty_result),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(24.dp),
            text = stringResource(R.string.empty_result),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
private fun LoadStateFooter(loadState: LoadState, onErrorStateRetryClicked: () -> Unit) {
    when (loadState) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.server_error_text),
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = onErrorStateRetryClicked
                ) {
                    Text(stringResource(R.string.try_again))
                }
            }
        }

        else -> Unit
    }
}