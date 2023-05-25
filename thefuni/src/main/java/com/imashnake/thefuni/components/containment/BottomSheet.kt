package com.imashnake.thefuni.components.containment

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
    screenHeightPx: Float,
    screenWidthPx: Float,
    initialHorizontalPadding: Float,
    anchors: Map<Float, BottomSheetState> = mapOf(
        // TODO: Why is this the other way around?
        0f to BottomSheetState.EXPANDED,
        screenHeightPx/2 to BottomSheetState.PEEK,
        screenHeightPx to BottomSheetState.HIDDEN,
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
        Column(
            modifier
                .statusBarsPadding()
                .align(Alignment.BottomCenter)
                .swipeable(
                    state = bottomSheetSwipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    // TODO: Fix this.
                    resistance = ResistanceConfig(Float.MIN_VALUE),
                    orientation = Orientation.Vertical
                )
                .drawBehind {
                    drawRoundRect(
                        color = sheetColor,
                        size = Size(
                            width = screenWidthPx - (bottomSheetSwipeableState.offset.value/screenHeightPx) * initialHorizontalPadding,
                            height = screenHeightPx
                        ),
                        cornerRadius = CornerRadius(
                            bottomSheetSwipeableState.offset.value/screenHeightPx * cornerRadius
                        ),
                        topLeft = Offset(
                            x = (bottomSheetSwipeableState.offset.value/screenHeightPx) * initialHorizontalPadding/2,
                            y = bottomSheetSwipeableState.offset.value/screenHeightPx * screenHeightPx
                        )
                    )
                }
                .offset {
                    IntOffset(
                        x = 0,
                        y = bottomSheetSwipeableState.offset.value.roundToInt()
                    )
                }
                .fillMaxWidth()
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
