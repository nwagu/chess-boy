buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.android.tools.build:gradle:${Versions.gradle_plugin_version}")
        classpath("com.squareup.sqldelight:gradle-plugin:${Versions.sql_delight_version}")
        classpath("com.google.gms:google-services:${Versions.google_services_version}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.firebase_crashlytics_version}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}