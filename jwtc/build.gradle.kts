plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.android.library)
}

version = "1.0"

kotlin {
    androidTarget()

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
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
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.nwagu.chessboy.jwtc"
    compileSdk = 37
    ndkVersion = "27.3.13750724"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testOptions {
            targetSdk = libs.versions.targetSdk.get().toInt()
        }
        externalNativeBuild {
            cmake {
                arguments += listOf("-DANDROID_ARM_NEON=TRUE", "-DANDROID_CPP_FEATURES=rtti exceptions", "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON")
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}