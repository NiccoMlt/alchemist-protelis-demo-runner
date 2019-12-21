plugins {
    kotlin("jvm") version "1.3.61"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("it.unibo.alchemist:alchemist-incarnation-protelis:9.3.0")
    implementation("it.unibo.alchemist:alchemist-loading:9.3.0")
    implementation("it.unibo.alchemist:alchemist-runner:9.3.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
