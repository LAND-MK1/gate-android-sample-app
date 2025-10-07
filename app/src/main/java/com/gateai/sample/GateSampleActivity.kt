package com.gateai.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gateai.sample.ui.GateSampleScreen
import com.gateai.sample.ui.theme.GateAISampleTheme

class GateSampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GateAISampleTheme {
                GateSampleScreen()
            }
        }
    }
}

