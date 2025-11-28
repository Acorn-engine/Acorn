plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":acorn-core"))
    implementation(project(":acorn-desktop"))
}