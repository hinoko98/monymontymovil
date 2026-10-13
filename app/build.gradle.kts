plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.monymontymovil"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.monymontymovil"
        // 26: las fuentes variables (Outfit, DM Sans) solo aplican el peso desde Android 8
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    // URL base de la API (Express). En el emulador, 10.0.2.2 es el localhost del PC.
    // Se puede cambiar sin tocar el código: ./gradlew assembleDebug -Pmonymonty.apiUrl=http://192.168.1.10:3000/
    val apiUrl = providers.gradleProperty("monymonty.apiUrl")

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"${apiUrl.getOrElse("http://10.0.2.2:3000/")}\"")
        }
        release {
            // TODO: poner la URL real de la API de producción (HTTPS) o pasarla con -Pmonymonty.apiUrl
            buildConfigField("String", "API_URL", "\"${apiUrl.getOrElse("https://REEMPLAZAR-URL-API/")}\"")
            isMinifyEnabled = false
            // TODO: Add your own signing config for the release build.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.10.01")
    implementation(composeBom)
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
