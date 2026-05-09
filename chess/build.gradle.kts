plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
}

version = "0.0.3"

kotlin {
    jvm()
    
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        allWarningsAsErrors.set(false)
    }

    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Chess game referee: Defines models and rules used to coordinate gameplay"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.uuid)
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
    namespace = "com.nwagu.chessboy.chess"
    compileSdk = 37
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testOptions {
            targetSdk = libs.versions.targetSdk.get().toInt()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

ext {
    set("publishGroupId", "com.nwagu.chess")
    set("publishArtifactId", "chess")
    set("publishVersion", "0.0.3")

    set("libraryName", "chess")
    set("libraryDescription", "Defines models and rules used to coordinate chess gameplay")
}

if (project.rootProject.file("local.properties").exists()) {
    apply { from("${rootProject.projectDir}/publish-maven.gradle") }
}
