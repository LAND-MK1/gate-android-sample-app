# Gate/AI Android Sample App

Sample Android application demonstrating the Gate/AI SDK integration with Jetpack Compose.

## Features

- 🎨 Modern Jetpack Compose UI
- 🔐 Gate/AI SDK integration example
- 📱 Material Design 3 components
- 🏗️ MVVM architecture with ViewModel
- ⚙️ BuildConfig-based configuration

## Setup

### 1. Configure Gate/AI Credentials

Update the BuildConfig fields in `app/build.gradle.kts`:

```kotlin
android {
    defaultConfig {
        buildConfigField("String", "GATE_BASE_URL", "\"https://yourteam.us01.gate-ai.net\"")
        buildConfigField("String", "SIGNING_CERT_SHA256", "\"YOUR_SHA256_FINGERPRINT\"")
        buildConfigField("String", "DEV_TOKEN", "\"\"")  // For emulator testing
    }
}
```

#### Get Your SHA-256 Fingerprint

**Debug keystore:**
```bash
keytool -list -v -keystore ~/.android/debug.keystore \
  -alias androiddebugkey \
  -storepass android \
  -keypass android | grep SHA256
```

**Release keystore:**
```bash
keytool -list -v -keystore /path/to/your/release.keystore \
  -alias your-key-alias
```

Copy the SHA256 value (e.g., `AA:BB:CC:...`) to the `SIGNING_CERT_SHA256` field.

### 2. (Optional) Setup Development Token

For testing on emulators where Play Integrity is unavailable:

1. Generate a development token in the Gate/AI Portal
2. Update `build.gradle.kts`:

```kotlin
buildTypes {
    debug {
        buildConfigField("String", "DEV_TOKEN", "\"your-dev-token-here\"")
    }
}
```

**⚠️ IMPORTANT**: Never include dev tokens in release builds!

### 3. Build and Run

```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or open in Android Studio and click Run.

## Project Structure

```
app/src/main/
├── AndroidManifest.xml
├── java/com/gateai/sample/
│   ├── GateSampleApp.kt         # Application class, SDK initialization
│   ├── GateSampleActivity.kt    # Main activity
│   └── ui/
│       ├── GateSampleScreen.kt  # Compose UI
│       └── GateSampleViewModel.kt # Business logic
└── res/
    └── values/
        ├── themes.xml            # Material theme
        └── strings.xml           # String resources
```

## How It Works

### 1. SDK Initialization (GateSampleApp.kt)

```kotlin
class GateSampleApp : Application() {
    lateinit var gateAIClient: GateAIClient

    override fun onCreate() {
        super.onCreate()
        
        val configuration = GateAIConfiguration(
            baseUrl = BuildConfig.GATE_BASE_URL,
            packageName = packageName,
            signingCertSha256 = BuildConfig.SIGNING_CERT_SHA256,
            developmentToken = BuildConfig.DEV_TOKEN.ifBlank { null },
            logLevel = if (BuildConfig.DEBUG) 
                GateAIConfiguration.LogLevel.DEBUG 
            else 
                GateAIConfiguration.LogLevel.INFO
        )

        gateAIClient = GateAIClient.create(this, configuration)
    }
}
```

### 2. Using the SDK (GateSampleViewModel.kt)

```kotlin
fun performSampleRequest() {
    viewModelScope.launch {
        try {
            val headers = gateAIClient.authorizationHeaders(
                path = "openai/models",
                method = HttpMethod.GET
            )
            // Headers ready to use with your HTTP client
        } catch (e: Exception) {
            // Handle error
        }
    }
}
```

### 3. UI (GateSampleScreen.kt)

```kotlin
@Composable
fun GateSampleScreen(viewModel: GateSampleViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        Button(onClick = { viewModel.performSampleRequest() }) {
            Text("Test Gate/AI Auth")
        }
        Text(uiState.statusText)
    }
}
```

## Customizing the Sample

### Change the API Endpoint

Modify the request path in `GateSampleViewModel.kt`:

```kotlin
val headers = gateAIClient.authorizationHeaders(
    path = "openai/chat/completions",  // Change this
    method = HttpMethod.POST
)
```

### Add Actual API Calls

Use the headers with your preferred HTTP client:

```kotlin
// With OkHttp
val request = Request.Builder()
    .url("${BuildConfig.GATE_BASE_URL}/openai/models")
    .apply {
        headers.forEach { (key, value) -> addHeader(key, value) }
    }
    .build()

// With Ktor
val response = client.get("${BuildConfig.GATE_BASE_URL}/openai/models") {
    headers {
        headers.forEach { (key, value) -> append(key, value) }
    }
}
```

### Change UI Theme

Edit `app/src/main/res/values/themes.xml` to customize the Material theme.

## Troubleshooting

### App Crashes on Startup

- Check that `GATE_BASE_URL` is valid
- Verify `SIGNING_CERT_SHA256` is correct
- Ensure your package name and cert are registered in Gate/AI Portal

### "Play Integrity Error" on Device

- Your app's package name must be registered in Gate/AI Portal
- SHA-256 certificate fingerprint must match registration
- App must be signed with the registered certificate

### "Play Integrity Error" on Emulator

- Use development token for emulator testing
- Set `DEV_TOKEN` in debug build variant
- Ensure dev token is valid and not expired

### "401 Unauthorized"

- Check that your Gate/AI base URL is correct
- Verify device clock is synchronized
- Check logs for DPoP nonce handling

## Dependencies

This sample uses:
- **Gate/AI SDK** (composite build from `../gate-android`)
- **Jetpack Compose** for UI
- **ViewModel** for state management
- **Kotlin Coroutines** for async operations

## Learning Resources

- [Gate/AI Android SDK README](../gate-android/README.md)
- [Compose Documentation](https://developer.android.com/jetpack/compose)
- [Android Architecture Components](https://developer.android.com/topic/architecture)

## Next Steps

1. Integrate with real AI API endpoints (OpenAI, Anthropic, etc.)
2. Add error handling UI feedback
3. Implement streaming responses
4. Add conversation history
5. Customize theme and branding

---

For more information, see the [main SDK documentation](../gate-android/README.md).
