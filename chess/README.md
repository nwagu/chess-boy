[![Maven Central](https://img.shields.io/maven-central/v/com.nwagu.chess/chess.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.nwagu.chess%22%20AND%20a:%22chess%22)

## A chess game library

### Usage

If you wish to use this library in a kotlin multiplatform module, add this to your module build.gradle.kts file:

```
val commonMain by getting {
    dependencies {
        // ...
        implementation("com.nwagu.chess:chess:<latest_version>")
    }
}
```

Otherwise, for use in an android project:

```
dependencies {
    // ...
    implementation("com.nwagu.chess:chess-android:<latest_version>")
}
```