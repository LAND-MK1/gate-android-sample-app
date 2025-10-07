package com.gateai.sample

import android.app.Application
import android.util.Log
import com.gateai.sdk.core.GateAIClient
import com.gateai.sdk.core.GateAIConfiguration

class GateSampleApp : Application() {
    lateinit var gateAIClient: GateAIClient
        private set

    override fun onCreate() {
        super.onCreate()
        gateAIClient = setupGateAIClient()
    }

    private fun setupGateAIClient(): GateAIClient {
        val config = GateAIConfiguration(
            baseUrl = BuildConfig.GATE_BASE_URL,
            packageName = packageName,
            signingCertSha256 = BuildConfig.SIGNING_CERT_SHA256,
            developmentToken = BuildConfig.DEV_TOKEN.takeIf { it.isNotBlank() }
        )
        return GateAIClient.create(
            context = this,
            configuration = config
        ).also {
            Log.i("GateSampleApp", "GateAIClient initialized")
        }
    }
}
