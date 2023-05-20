package com.imashnake.thefuni.styles.typography

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier
) {
    BasicText(
        text = text,
        modifier = modifier
    )
}
