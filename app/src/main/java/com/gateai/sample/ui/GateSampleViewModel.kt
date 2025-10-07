package com.gateai.sample.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gateai.sample.BuildConfig
import com.gateai.sample.GateSampleApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GateSampleViewModel(application: Application) : AndroidViewModel(application) {

    private val gateAIClient: com.gateai.sdk.core.GateAIClient
        get() = (getApplication<Application>() as GateSampleApp).gateAIClient

    private val _uiState = MutableStateFlow(GateSampleUiState())
    val uiState: StateFlow<GateSampleUiState> = _uiState.asStateFlow()

    fun performSampleRequest() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, statusText = "Calling Gate/AI...")
            try {
                val headers = gateAIClient.authorizationHeaders(
                    path = "openai/models",
                    method = com.gateai.sdk.core.HttpMethod.GET
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    statusText = "Success! Headers: ${headers.keys}"
                )
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    statusText = "Error: ${t.message ?: "Unknown error"}"
                )
            }
        }
    }
}

data class GateSampleUiState(
    val isLoading: Boolean = false,
    val statusText: String = "Tap the button to call Gate/AI Proxy"
)
