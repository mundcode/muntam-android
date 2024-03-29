package com.mundcode.designsystem.components.bottomsheets

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mundcode.designsystem.components.bottomsheets.option.SubjectOptionBottomSheetContent
import com.mundcode.designsystem.components.buttons.PrimaryMTButton
import com.mundcode.designsystem.theme.MTBottomSheetBackground
import com.mundcode.designsystem.theme.Transparent
import com.mundcode.designsystem.theme.White

@Composable
fun MTBottomSheets(
    show: Boolean,
    modifier: Modifier = Modifier,
    onClickOutSide: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val height = LocalConfiguration.current.screenHeightDp
    val density = LocalDensity.current
    val systemUiController = rememberSystemUiController()

    val backgroundColor by animateColorAsState(
        targetValue = if (show) {
            MTBottomSheetBackground
        } else {
            Transparent
        }
    )

    val statusBarColor by animateColorAsState(
        targetValue = if (show) {
            MTBottomSheetBackground
        } else {
            White
        }
    )

    BackHandler(enabled = show) {
        // do nothing
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    )

    systemUiController.setStatusBarColor(color = statusBarColor)

    AnimatedVisibility(
        visible = show,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        enter = slideInVertically { contentHeight ->
            with(density) {
                height.dp.roundToPx() + contentHeight
            }
        },
        exit = slideOutVertically { _ ->
            with(density) {
                height.dp.roundToPx()
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = onClickOutSide,
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun MTBottomSheetsPreview() {
    var show by remember {
        mutableStateOf(false)
    }

    PrimaryMTButton(
        text = "보여주기", onClick = { show = true }
    )

    MTBottomSheets(
        show = show,
        onClickOutSide = { show = false }
    ) {
        SubjectOptionBottomSheetContent({ show = false }, { show = false }, { show = false })
    }
}
