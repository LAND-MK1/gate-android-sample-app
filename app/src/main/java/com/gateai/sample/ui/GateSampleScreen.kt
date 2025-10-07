package com.gateai.sample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gateai.sample.ui.theme.GateAISampleTheme

@Composable
fun GateSampleScreen(
    viewModel: GateSampleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    GateSampleContent(
        state = uiState,
        onTriggerRequest = viewModel::performSampleRequest
    )
}

@Composable
private fun GateSampleContent(
    state: GateSampleUiState,
    onTriggerRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues(24.dp)),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gate/AI Android Sample",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = state.statusText,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Button(
            enabled = !state.isLoading,
            onClick = onTriggerRequest
        ) {
            Text("Try Sample Request")
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}

@GatePreview
@Composable
private fun GateSampleContentPreview() {
    GateAISampleTheme {
        GateSampleContent(
            state = GateSampleUiState(),
            onTriggerRequest = {}
        )
    }
}
