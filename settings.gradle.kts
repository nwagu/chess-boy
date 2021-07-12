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
include(":bluetoothchat")
include(":jwtc")
include(":stockfish")
include(":igel")
include(":sharedmodels")
