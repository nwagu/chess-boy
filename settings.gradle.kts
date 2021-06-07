pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    
}
rootProject.name = "ChessBoy"

include(":androidApp")
include(":chess")
include(":chessengineintegration")
include(":bluetoothchat")
include(":jwtc")
