plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.rifqi.trackfunds.core.domain" // Sesuaikan dengan nama paket Anda
    compileSdk = 35 // Gunakan versi compileSdk yang sama dengan modul :app Anda

    defaultConfig {
        minSdk = 26 // Samakan dengan minSdk proyek Anda
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies{
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)
}