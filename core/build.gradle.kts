plugins {
    id "java"
    id "org.jetbrains.kotlin.jvm"
}

project.ext.lwjglVersion =
        "3.3.0-SNAPSHOT"

project.ext.lwjglModules = [
        "lwjgl",
        "lwjgl-glfw",
        "lwjgl-opengl",
        "lwjgl-stb"
]
project.ext.lwjglPlatforms = [
        "natives-linux",
        "natives-linux-arm32",
        "natives-linux-arm64",
        "natives-macos",
        "natives-macos-arm64",
        "natives-windows-arm64",
        "natives-windows-x86",
        "natives-windows"
]

group = "com.husker.minui"
version = "0.1"

repositories {
    mavenCentral()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}

sourceSets {
    main {
        resources { srcDirs "src/main/resources" }
    }
    examples {
        resources { srcDirs "src/examples/resources" }
        java {
            srcDirs("src/examples/kotlin")
            compileClasspath += sourceSets.main.runtimeClasspath
            runtimeClasspath += sourceSets.main.runtimeClasspath
        }
    }
}

dependencies {
    testImplementation "org.junit.jupiter:junit-jupiter-api:5.8.1"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine:5.8.1"

    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.31"
    implementation platform("org.lwjgl:lwjgl-bom:$lwjglVersion")

    for(module in lwjglModules){
        implementation "org.lwjgl:$module:$lwjglVersion"
        for(platform in lwjglPlatforms)
            runtimeOnly "org.lwjgl:$module:$lwjglVersion:$platform"
    }
}

test {
    useJUnitPlatform()
    forkEvery = 1
    maxParallelForks = 1
}