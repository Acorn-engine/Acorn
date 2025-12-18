# Acorn 🌰

Acorn is a lightweight, Kotlin based 2D game engine in early development, built on LWJGL
> ⚠️ **Status:** Pre-alpha. Things will change frequently, there is currently no stable release, it is __not__ recommended to use this for production use yet.

---

## Goals

- **Simple game code**
- **Clean separation**
- **Modern OpenGL**
- **No fluff**

---

## Project Structure

```
Acorn/
  acorn-core/      # Engine API, scene/components, math, assets, input, events, etc
  acorn-desktop/   # GLFW windowing, OpenGL renderer, pipelines, texture loading
  acorn-sample/    # Example game that uses the engine
```

---

## Requirements

- **JDK 17+** (recommended: 17 or 21)
- **Gradle** (wrapper included)
- **macOS note:** GLFW requires `-XstartOnFirstThread`

---

## Running the Sample

From the repo root:
```bash
./gradlew :acorn-sample:run
```

If you’re on macOS, the project is configured to run with:

- `-XstartOnFirstThread`
- `--enable-native-access=ALL-UNNAMED`

(These are set in the `application` block for desktop/sample modules)

---

## Where to Put Assets

Place images in:
`acorn-sample/src/main/resources/`

Then load them by filename:
```kotlin
val playerSprite = sprite("player.png")
```

Supported formats: `.png`, `.jpg` (via STB)

---

## Minimal Example
```kotlin
class TestGame : AcornGame() {
    private lateinit var player: GameObject

    override fun onStart() {
        player = scene.spriteObject(sprite("player.pnf"), center(), Vec2(128f, 128f))
    }

    override fun onUpdate(dt: Float) {
        val dir = input.axis2D(Keys.A, Keys.D, Keys.S, Keys.W).normalized()
        player.transform.position += dir * (250f * dt)
    }
}
```

---

## Contributing

PRs and issues are more than welcome, but keep in mind this engine is in the foundational phase, just make sure your code is clean and structured.
