package com.mundcode.designsystem.components.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius4
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.designsystem.theme.Gray600
import com.mundcode.designsystem.theme.MTGreen
import com.mundcode.designsystem.theme.MTLightGreen
import com.mundcode.designsystem.theme.MTLightOrange
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle

@Composable
private fun SmallMTTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(color = backgroundColor, shape = CornerRadius4)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        style = MTTextStyle.textBold13,
        color = textColor,
        maxLines = 1
    )
}

@Composable
private fun BigMTTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .background(color = backgroundColor, shape = CornerRadius4)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = MTTextStyle.textBold14,
        color = textColor,
        maxLines = 1
    )
}

@Composable
fun SubjectNameTag(
    name: String,
    modifier: Modifier = Modifier,
    isSmall: Boolean = true,
) {
    if (isSmall) {
        SmallMTTag(
            text = name,
            backgroundColor = MTLightOrange,
            textColor = MTOrange,
            modifier = modifier
        )
    } else {
        BigMTTag(
            text = name,
            backgroundColor = MTLightOrange,
            textColor = MTOrange,
            modifier = modifier
        )
    }
}

@Composable
fun RunningTag(isSmall: Boolean = true) {
    // todo string ????????????
    if (isSmall) {
        SmallMTTag(text = "?????????", backgroundColor = MTLightGreen, textColor = MTGreen)
    } else {
        BigMTTag(text = "?????????", backgroundColor = MTLightGreen, textColor = MTGreen)
    }
}

@Composable
fun FinishedTag(isSmall: Boolean = true) {
    // todo string ????????????
    if (isSmall) {
        SmallMTTag(text = "??????", backgroundColor = Gray200, textColor = Gray600)
    } else {
        BigMTTag(text = "??????", backgroundColor = Gray200, textColor = Gray600)
    }
}

@Preview
@Composable
fun MTTagPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        SmallMTTag(text = "?????????", backgroundColor = MTLightOrange, textColor = MTOrange)
        SmallMTTag(text = "?????????", backgroundColor = MTLightGreen, textColor = MTGreen)
        SmallMTTag(text = "?????????", backgroundColor = MTLightGreen, textColor = MTGreen)
        BigMTTag(text = "?????????", backgroundColor = MTLightGreen, textColor = MTGreen)
        BigMTTag(text = "?????????", backgroundColor = MTLightOrange, textColor = MTOrange)

        SubjectNameTag("?????? ??????")
        SubjectNameTag("??????")
        SubjectNameTag("TOEIC")
        SubjectNameTag("TOEIC", isSmall = false)
        RunningTag()
        RunningTag(true)
        FinishedTag()
        FinishedTag(true)
    }
}
