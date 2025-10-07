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
                // Use performProxyRequest to test complete flow with DPoP nonce retry
                val response = gateAIClient.performProxyRequest(
                    path = "openai/v1/models",
                    method = com.gateai.sdk.core.HttpMethod.GET
                )
                
                val responseBody = response.body
                val statusText = buildString {
                    appendLine("✅ Success!")
                    appendLine("Status: ${response.status}")
                    if (responseBody != null) {
                        appendLine("\nResponse preview:")
                        appendLine(responseBody.take(200) + "...")
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    statusText = statusText
                )
            } catch (t: Throwable) {
                val errorText = buildString {
                    appendLine("❌ Error:")
                    
                    // Check if it's a GateApiException to show detailed error info
                    if (t is com.gateai.sdk.network.GateApiException) {
                        appendLine("\nHTTP Status: ${t.statusCode}")
                        
                        val errorBody = t.body
                        if (errorBody != null && errorBody.isNotBlank()) {
                            appendLine("\nServer Response:")
                            appendLine(errorBody)
                        }
                        
                        if (t.headers.isNotEmpty()) {
                            appendLine("\nResponse Headers:")
                            t.headers.forEach { (key, value) ->
                                appendLine("  $key: $value")
                            }
                        }
                    } else {
                        appendLine(t.message ?: "Unknown error")
                        appendLine("\nError Type: ${t::class.simpleName}")
                        appendLine("\nStack trace (first 500 chars):")
                        appendLine(t.stackTraceToString().take(500))
                    }
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    statusText = errorText
                )
            }
        }
    }
}

data class GateSampleUiState(
    val isLoading: Boolean = false,
    val statusText: String = "Tap the button to call Gate/AI Proxy"
)
