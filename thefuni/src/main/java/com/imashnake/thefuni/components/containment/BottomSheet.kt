package com.imashnake.thefuni.components.containment

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ResistanceConfig
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    handle: @Composable ColumnScope.() -> Unit = {
        Surface(
            shape = RoundedCornerShape(percent = 50),
            color = Color.Black.copy(alpha = 0.5f),
            modifier = Modifier
                .padding(12.dp)
                .width(32.dp)
                .height(4.dp)
                .align(Alignment.CenterHorizontally)
        ) {  }
    },
    cornerRadius: Float,
    scrimColor: Color = Color.Black.copy(alpha = 0.5f),
    sheetColor: Color,
    anchors: Map<Float, BottomSheetState> = mapOf(
        // TODO: Why is this the other way around?
        0f to BottomSheetState.EXPANDED,
        with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx()/2 }.toFloat() to BottomSheetState.PEEK,
        with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }.toFloat() to BottomSheetState.HIDDEN,
    ),
    bottomSheetSwipeableState: SwipeableState<BottomSheetState> = rememberSwipeableState(
        BottomSheetState.HIDDEN
    ),
    content: @Composable () -> Unit
) {
    val animatedScrimColor by animateColorAsState(
        targetValue = if (bottomSheetSwipeableState.currentValue == BottomSheetState.HIDDEN) {
            Color.Transparent
        } else scrimColor,
        label = "animate_scrim_color"
    )

    Box(
        Modifier
            .fillMaxSize()
            .drawBehind { drawRect(animatedScrimColor) }
    ) {
        val progress = bottomSheetSwipeableState.offset.value/(with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }.toFloat())
        val animatedHorizontalPadding = (progress * 16).dp
        val animatedCornerRadius = (progress * cornerRadius).dp
        Column(
            modifier
                .statusBarsPadding()
                .padding(horizontal = animatedHorizontalPadding)
                .align(Alignment.BottomCenter)
                .swipeable(
                    state = bottomSheetSwipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    // TODO: Fix this.
                    resistance = ResistanceConfig(Float.MIN_VALUE),
                    orientation = Orientation.Vertical
                )
                .offset { IntOffset(0, bottomSheetSwipeableState.offset.value.roundToInt()) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = animatedCornerRadius, topEnd = animatedCornerRadius))
                .background(sheetColor)
        ) {
            handle()
            content()
        }
    }
}

enum class BottomSheetState {
    HIDDEN,
    PEEK,
    EXPANDED,
}
