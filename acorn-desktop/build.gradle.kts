plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":acorn-core"))

    val lwjglVersion = "3.3.6"
    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")

    val os = org.gradle.internal.os.OperatingSystem.current()
    val lwjglNatives = when {
        os.isWindows -> "natives-windows"
        os.isMacOsX -> "natives-macos"
        os.isLinux -> "natives-linux"
        else -> error("Unsupported operating system: $os")
    }

    runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("dev.acorn.desktop.DesktopLauncherKt")
}