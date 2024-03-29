plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version "1.6.10"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.nwagu.android.chessboy"
        minSdk = 21
        targetSdk = 33
        versionCode = 19
        versionName = "3.6.3"
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
        freeCompilerArgs += listOf(
//            "-P",
//            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true",
            "-Xjvm-default=enable"
        )
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_version
    }
}

dependencies {
    implementation(project(":sharedmodels"))

    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.4.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

    implementation("androidx.compose.runtime:runtime:${Versions.compose_version}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose_version}")
    implementation("androidx.compose.ui:ui-tooling:${Versions.compose_version}")
    implementation("androidx.compose.foundation:foundation:${Versions.compose_version}")
    implementation("androidx.compose.foundation:foundation-layout:${Versions.compose_version}")
    implementation("androidx.compose.material:material:${Versions.compose_version}")
    implementation("androidx.compose.animation:animation:${Versions.compose_version}")
    implementation("androidx.navigation:navigation-compose:2.5.0-alpha01")

    implementation(platform("com.google.firebase:firebase-bom:28.0.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    implementation("com.karumi:dexter:6.2.2")
    implementation("com.jakewharton.timber:timber:4.7.1")

}