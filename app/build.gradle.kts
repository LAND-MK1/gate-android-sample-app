plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.gateai.sample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gateai.sample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        // Default to placeholder values (will be overridden in buildTypes)
        buildConfigField("String", "GATE_BASE_URL", "\"https://yourteam.us01.gate-ai.net\"")
        buildConfigField("String", "SIGNING_CERT_SHA256", "\"\"")
        buildConfigField("String", "DEV_TOKEN", "\"\"")
    }

    buildTypes {
        debug {
            // Automatically use the debug keystore SHA-256
            buildConfigField("String", "SIGNING_CERT_SHA256", "\"EA:75:52:B2:14:5B:4B:3E:53:DE:37:95:7D:E7:70:A9:4E:6E:B8:5D:D4:D2:0E:B0:1C:66:02:1F:C2:33:DC:89\"")
            // Optionally set a dev token for emulator testing
            buildConfigField("String", "DEV_TOKEN", "\"\"") // Add your dev token here if testing on emulator
        }
        
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // For release, you must configure your actual release keystore SHA-256
            buildConfigField("String", "SIGNING_CERT_SHA256", "\"<YOUR_RELEASE_SHA256>\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("com.gateai.sdk:gateai")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)

    debugImplementation(libs.androidx.compose.ui.tooling)
}

