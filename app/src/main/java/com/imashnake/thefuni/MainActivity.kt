package com.imashnake.thefuni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.imashnake.thefuni.components.containment.BottomSheet
import com.imashnake.thefuni.styles.typography.Text
import com.imashnake.thefuni.ui.theme.TheFuniTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }

            val screenHeightPx = with(LocalDensity.current) {
                LocalConfiguration.current.screenHeightDp.dp.toPx()
            }

            val screenWidthPx = with(LocalDensity.current) {
                LocalConfiguration.current.screenWidthDp.dp.toPx()
            }

            val initialHorizontalPaddingPx = 200f
            val initialHorizontalPaddingDp = with(LocalDensity.current) {
                initialHorizontalPaddingPx.toDp()
            }

            TheFuniTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomSheet(
                        cornerRadius = 100f,
                        sheetColor = Color.Green,
                        screenHeightPx = screenHeightPx,
                        screenWidthPx = screenWidthPx,
                        initialHorizontalPadding = 200f
                    ) {
                        Text(
                            text = "Hello",
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 24.dp)
                                .padding(horizontal = initialHorizontalPaddingDp/2 + 6.dp)
                        )
                    }
                }
            }
        }
    }
}
