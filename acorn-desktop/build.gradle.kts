plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":acorn-core"))

    val lwjglVersion = "3.3.6"
    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")

    val os = org.gradle.internal.os.OperatingSystem.current()
    val arch = System.getProperty("os.arch")

    val lwjglNatives = when {
        os.isWindows && arch.contains("64") -> "natives-windows"
        os.isLinux && arch.contains("64")   -> "natives-linux"
        os.isMacOsX && arch == "aarch64"            -> "natives-macos-arm64"
        os.isMacOsX                                 -> "natives-macos"
        else -> error("Unsupported platform: ${os.name} $arch")
    }

    runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")

    testImplementation(kotlin("test"))
}