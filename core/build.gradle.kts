import java.net.URI

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
}

group = "com.husker.minui"
version = "0.1"

val versions = hashMapOf(
    "jupiter"   to "5.8.1"
)

repositories {
    mavenCentral()
    maven { url = URI("https://oss.sonatype.org/content/repositories/snapshots/") }
}

sourceSets {
    main {
        resources { srcDirs += File("src/main/resources") }
    }

    create("examples") {
        resources { srcDirs += File("src/examples/resources") }
        java {
            srcDirs("src/examples/kotlin")
            compileClasspath += sourceSets.main.get().runtimeClasspath
            runtimeClasspath += sourceSets.main.get().runtimeClasspath
        }
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${versions["jupiter"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${versions["jupiter"]}")

    implementation(platform("org.lwjgl:lwjgl-bom:3.3.0-SNAPSHOT"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-opengl")
    runtimeOnly("org.lwjgl:lwjgl::natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-opengl::natives-windows")

    // For debug
    //compileOnly("org.lwjgl:lwjgl-opengl:3.3.0-SNAPSHOT")
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Main-Class"] = "com.husker.MainKt"
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
}

