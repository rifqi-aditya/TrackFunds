import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.rifqi.trackfunds.core.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "GEMINI_API_KEY",
                gradleLocalProperties(
                    rootDir,
                    providers
                ).getProperty("GEMINI_API_KEY")
            )
        }
        debug {
            buildConfigField(
                "String",
                "GEMINI_API_KEY",
                gradleLocalProperties(
                    rootDir,
                    providers
                ).getProperty("GEMINI_API_KEY")
            )
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
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    // Hilt untuk Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room untuk Database Lokal
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Network (Retrofit & OkHttp)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx.serialization) // Untuk parsing JSON
    implementation(libs.okhttp.logging.interceptor) // Untuk debugging network call di Logcat

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.runtime)

    // Android Utilities
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}