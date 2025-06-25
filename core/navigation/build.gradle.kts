plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.rifqi.trackfunds.core.navigation"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation:api"))
    implementation(project(":feature:home"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:account"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:transaction"))
    implementation(project(":feature:budget"))
    implementation(project(":feature:scan"))


    // Pastikan Anda menggunakan Compose BOM untuk mengelola versi secara konsisten
    implementation(platform(libs.androidx.compose.bom)) // Gunakan versi terbaru yang stabil

    // Core AndroidX dan Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Compose Material3 (untuk Icon, NavigationBar, NavigationBarItem, Text)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Compose Foundation (untuk Box, fillMaxSize)
    implementation(libs.androidx.foundation)

    // Compose Navigation (untuk NavHost, composable, rememberNavController, dll.)
    implementation(libs.androidx.navigation.compose) // Gunakan versi terbaru yang stabil

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Debugging (hanya untuk development)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}