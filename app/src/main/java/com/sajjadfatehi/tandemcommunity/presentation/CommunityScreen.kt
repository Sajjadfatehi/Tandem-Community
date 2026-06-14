package com.sajjadfatehi.tandemcommunity.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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
    LazyColumn(modifier = modifier) {
        items(
            count = communityMembers.itemCount,
            key = { index -> communityMembers[index]?.id ?: index }
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
            //TODO: handle paging error state and retry button
        }
    }
}

@Composable
private fun CommunityMemberItem(
    member: CommunityMember,
    onLikeClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable(onClick = onLikeClick)
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
                    Text(
                        text = member.firstName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    if (member.isNew) {
                        Text(
                            text = "NEW",
                            modifier = Modifier
                                .background(
                                    color = Color.Magenta,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                ),
                            style = TextStyle(color = Color.White)
                        )
                    } else {
                        Text(member.referenceCnt.toString())
                    }
                }

                Text(
                    text = member.topic,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                //TODO:manage different styles later
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Native-${member.natives.joinToString()}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                        text = "Learns-${member.learns.joinToString()}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
    }
}

@Composable
private fun ProfileImage(member: CommunityMember) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = member.pictureUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
