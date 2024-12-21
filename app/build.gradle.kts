plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.todo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.todo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildToolsVersion = "35.0.0"

    buildTypes {
        debug {
            buildConfigField("String", "HA_TOKEN", "\"${project.findProperty("HA_TOKEN") ?: ""}\"")
            buildConfigField("String", "HA_SERVER_URL", "\"${project.findProperty("HA_SERVER_URL") ?: ""}\"")
        }
        release {
            buildConfigField("String", "HA_TOKEN", "\"${project.findProperty("HA_TOKEN") ?: ""}\"")
            buildConfigField("String", "HA_SERVER_URL", "\"${project.findProperty("HA_SERVER_URL") ?: ""}\"")
        }
    }
}

dependencies {
    // AndroidX Core i Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation)
    debugImplementation(libs.ui.tooling)

    // Placeholder Material3
    implementation(libs.accompanist.placeholder.material3)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)

    // Room Database - zaktualizowane do KSP
    implementation("androidx.room:room-runtime:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0") // Zamiana kapt na ksp
    implementation("androidx.room:room-ktx:2.6.0")

    // WebSocket
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    //zależności Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Testy
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}