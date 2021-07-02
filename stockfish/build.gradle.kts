import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "stockfish"
        podfile = project.file("../iosApp/Podfile")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //
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
                //
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdk = 30
    ndkVersion = "21.0.6113669"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 16
        targetSdk = 30
        externalNativeBuild {
            cmake {
                abiFilters += listOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
                arguments += listOf("-DANDROID_TOOLCHAIN=clang", "-DANDROID_STL=c++_static")
                cppFlags += listOf("-std=c++11 -frtti -fexceptions -DNDEBUG")
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

project.afterEvaluate {
    tasks["javaPreCompileDebug"].dependsOn(tasks["externalNativeBuildDebug"])
}
