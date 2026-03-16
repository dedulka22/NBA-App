package com.example.nbaapp.ui.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nbaapp.R

/**
 * Generic error screen component.
 *
 * @param messageResId The string resource ID for the error message
 * @param formatArgs Optional format arguments for the string resource
 * @param onRetry Optional callback for retry action
 */
@Composable
fun ErrorScreen(
    @StringRes messageResId: Int,
    formatArgs: List<Any> = emptyList(),
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = messageResId, *formatArgs.toTypedArray()),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        onRetry?.let {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = it) {
                Text(stringResource(id = R.string.retry))
            }
        }
    }
}
