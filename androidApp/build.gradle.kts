plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "com.nwagu.android.chessboy"
        minSdk = 21
        targetSdk = 30
        versionCode = 10
        versionName = "3.0.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }
    // Set both the Java and Kotlin compilers to target Java 8.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_version
    }
}

dependencies {
    implementation(project(":bluetoothchat"))
    implementation(project(":chess"))
    implementation(project(":jwtc"))
    implementation(project(":chessengineintegration"))

    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.activity:activity-compose:1.3.0-alpha08")
    implementation("androidx.fragment:fragment-ktx:1.3.4")

    implementation("androidx.compose.runtime:runtime:${Versions.compose_version}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose_version}")
    implementation("androidx.compose.ui:ui-tooling:${Versions.compose_version}")
    implementation("androidx.compose.foundation:foundation:${Versions.compose_version}")
    implementation("androidx.compose.foundation:foundation-layout:${Versions.compose_version}")
    implementation("androidx.compose.material:material:${Versions.compose_version}")
    implementation("androidx.compose.animation:animation:${Versions.compose_version}")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha01")

    implementation(platform("com.google.firebase:firebase-bom:28.0.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    implementation("com.karumi:dexter:6.2.2")
    implementation("com.jakewharton.timber:timber:4.7.1")

}