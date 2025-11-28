plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":acorn-core"))
    implementation(project(":acorn-desktop"))
}

application {
    mainClass.set("dev.acorn.sample.MainKt")

    applicationDefaultJvmArgs = listOf(
        "-XstartOnFirstThread",
        "--enable-native-access=ALL-UNNAMED"
    )
}