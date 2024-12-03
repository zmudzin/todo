import java.util.Properties
import java.io.FileInputStream

// Wczytaj token z pliku local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
val haToken = localProperties.getProperty("HA_TOKEN") ?: ""

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
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
        println("Loaded HA_TOKEN: $haToken")
        buildConfigField("String", "HA_TOKEN", "\"${haToken}\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // lub nowsza wersja
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "35.0.0"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.compose.ui:ui:1.7.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.5")
    implementation("androidx.compose.foundation:foundation:1.7.5")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
}
