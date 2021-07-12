[![Maven Central](https://img.shields.io/maven-central/v/com.nwagu.chess/chess.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.nwagu.chess%22%20AND%20a:%22chess%22)

## A chess game library

### Usage

To use this lib in a kotlin multiplatform module, add this to the build.gradle.kts file:

```
val commonMain by getting {
    dependencies {
        // ...
        implementation("com.nwagu.chess:chess:<latest_version>")
    }
}
```

To use it in an android project, use:

```
dependencies {
    // ...
    implementation("com.nwagu.chess:chess-android:<latest_version>")
}
```