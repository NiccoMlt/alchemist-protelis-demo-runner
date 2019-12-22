import de.fayard.OrderBy.GROUP_AND_ALPHABETICAL

plugins {
    kotlin("jvm") version Versions.org_jetbrains_kotlin_jvm_gradle_plugin
    kotlin("kapt") version Versions.org_jetbrains_kotlin_kapt_gradle_plugin
    id("de.fayard.refreshVersions") version Versions.de_fayard_refreshversions_gradle_plugin
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(Libs.alchemist_incarnation_protelis)
    implementation(Libs.alchemist_loading)
    implementation(Libs.alchemist_engine)
    implementation(Libs.alchemist_runner)
    implementation(Libs.alchemist_time)
    implementation(Libs.konf)
    implementation(Libs.moshi)
    implementation(Libs.moshi_kotlin)
    kapt(Libs.moshi_kotlin_codegen)

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
