plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "com.warasugitewara"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("com.googlecode.lanterna:lanterna:3.1.1")
    
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.23")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.warasugitewara.monitor.AppKt")
}

tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        attributes("Main-Class" to "com.warasugitewara.monitor.AppKt")
    }
}
