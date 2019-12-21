import de.fayard.OrderBy.GROUP_AND_ALPHABETICAL

plugins {
    kotlin("jvm") version Versions.org_jetbrains_kotlin_jvm_gradle_plugin
    id("de.fayard.refreshVersions") version Versions.de_fayard_buildsrcversions_gradle_plugin
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(Libs.alchemist_incarnation_protelis)
    implementation(Libs.alchemist_loading)
    implementation(Libs.alchemist_runner)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

buildSrcVersions {
    orderBy = GROUP_AND_ALPHABETICAL
}
