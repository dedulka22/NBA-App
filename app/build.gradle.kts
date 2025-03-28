plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
            buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY") ?: "default_key"}\"")
            buildConfigField("String", "CLIENT_ID", "\"${project.findProperty("CLIENT_ID") ?: "default_key"}\"")
        }
        release {
            buildConfigField("String", "API_KEY", "\"${project.findProperty("API_KEY") ?: "default_key"}\"")
            buildConfigField("String", "CLIENT_ID", "\"${project.findProperty("CLIENT_ID") ?: "default_key"}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        unitTests.all {
            it.useJUnitPlatform()
        }
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

    // Voyager for navigation
    implementation(libs.voyager.navigator)

    // Glide for image loading
    implementation(libs.bumptech.glide)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Mockk for mocking
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}