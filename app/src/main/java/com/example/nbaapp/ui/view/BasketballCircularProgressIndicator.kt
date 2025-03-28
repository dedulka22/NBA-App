package com.example.nbaapp.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nbaapp.R

/**
 * Circular progress indicator with a basketball image
 */
@Composable
fun BasketballCircularProgressIndicator() {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000)
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 16.dp,
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        )

        Image(
            painter = painterResource(id = R.drawable.basketball_ball),
            contentDescription = "Basketball Loading",
            modifier = Modifier
                .size(100.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBasketballCircularProgressIndicator() {
    BasketballCircularProgressIndicator()
}
