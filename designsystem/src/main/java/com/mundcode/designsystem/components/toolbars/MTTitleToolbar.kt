package com.mundcode.designsystem.components.toolbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.R
import com.mundcode.designsystem.components.etc.Margin
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTTextStyle

@Composable
fun MTTitleToolbar(
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    onClickBack: () -> Unit = {},
    backEnabled: Boolean = true,
    showBottomDivider: Boolean = true,
    title: String = "",
    icons: List<@Composable () -> Unit> = listOf()
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(44.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_24_dp),
                        contentDescription = null,
                        tint = Gray900,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .clickable(
                                onClick = onClickBack,
                                indication = null,
                                interactionSource = MutableInteractionSource(),
                                enabled = backEnabled
                            )
                            .padding(start = 20.dp)
                    )
                    Margin(dp = 12.dp)
                }

                if (!showBack) {
                    Margin(dp = 20.dp)
                }

                Text(
                    text = title,
                    style = MTTextStyle.textBold20,
                    color = Gray900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Margin(dp = 20.dp)

            Row {
                icons.forEach {
                    it()
                }
            }
        }

        if (showBottomDivider) {
            Divider(modifier = Modifier.fillMaxWidth(), color = Gray200)
        }
    }
}
