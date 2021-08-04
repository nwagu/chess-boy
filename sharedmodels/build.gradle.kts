import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {
        binaries
            .filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>()
            .forEach {
                it.transitiveExport = true
                it.export(project(":chess"))
                it.export(project(":bluetoothchat"))
            }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "sharedmodels"
        podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
                api(project(":bluetoothchat"))
                api(project(":chess"))
                implementation("com.squareup.sqldelight:runtime:${Versions.sql_delight_version}")
                implementation(project(":jwtc"))
                implementation(project(":stockfish"))
                api("co.touchlab:kermit:0.1.9") // for logging
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core:1.6.0")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
                implementation("com.squareup.sqldelight:android-driver:${Versions.sql_delight_version}")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:${Versions.sql_delight_version}")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 16
        targetSdk = 30
    }
}

sqldelight {
    database("ChessBoyDatabase") {
        packageName = "com.nwagu.chessboy"
    }
}