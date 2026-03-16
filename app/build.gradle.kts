import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kover)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    namespace = "com.example.nbaapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nbaapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Remove LICENSE files from APK because of Testing
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY", "default_key")}\"")
            buildConfigField("String", "CLIENT_ID", "\"${localProperties.getProperty("CLIENT_ID", "default_key")}\"")
        }
        release {
            buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY", "default_key")}\"")
            buildConfigField("String", "CLIENT_ID", "\"${localProperties.getProperty("CLIENT_ID", "default_key")}\"")
            isMinifyEnabled = true // Enable code minification
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = true // Enable resource shrinking
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Paging 3
    implementation(libs.paging.compose)
    implementation(libs.paging.runtime)

    // Koin for DI
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Navigation Compose
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Room for offline caching
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // Glide for image loading
    implementation(libs.bumptech.glide)

    // Test dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Mockk for mocking
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}