package dev.acorn.desktop.gl.shader

/**
 * Centralized GLSL source strings used by the [dev.acorn.desktop.render.DesktopRenderer]
 */
object ShaderSources {
    const val VERT = """
        #version 330 core
        layout(location = 0) in vec2 aPos;
        layout(location = 1) in vec2 aUV;

        uniform mat4 uProjection;
        uniform mat4 uModel;

        out vec2 vUV;
        out vec2 vLocal;

        void main() {
            vUV = aUV;
            vLocal = aPos; // -0.5..0.5
            gl_Position = uProjection * uModel * vec4(aPos, 0.0, 1.0);
        }
    """

    const val FRAG = """
        #version 330 core
        in vec2 vUV;
        in vec2 vLocal;

        uniform vec4 uColor;
        uniform int uUseTexture;
        uniform sampler2D uTex;

        // 0 none, 1 circle
        uniform int uMaskType;

        out vec4 FragColor;

        void main() {
            if (uMaskType == 1) {
                // vLocal is -0.5..0.5, scale to -1..1
                vec2 p = vLocal * 2.0;
                if (dot(p, p) > 1.0) discard;
            }

            vec4 base = uColor;
            if (uUseTexture == 1) {
                base *= texture(uTex, vUV);
            }
            FragColor = base;
        }
    """

    const val LINE_VERT = """
        #version 330 core
        layout(location = 0) in vec2 aPos;
        
        uniform mat4 uProjection;
        
        void main() {
            gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        }
    """

    const val LINE_FRAG = """
        #version 330 core
        uniform vec4 uColor;
        out vec4 FragColor;

        void main() {
            FragColor = uColor;
        }
    """

    const val BATCH_VERT = """
        #version 330 core
        layout(location = 0) in vec2 aPos;
        layout(location = 1) in vec2 aUV;
        layout(location = 2) in vec4 aColor;
        layout(location = 3) in vec2 aLocal;
        layout(location = 4) in float aMaskType;

        uniform mat4 uProjection;

        out vec2 vUV;
        out vec4 vColor;
        out vec2 vLocal;
        flat out int vMaskType;

        void main() {
            vUV = aUV;
            vColor = aColor;
            vLocal = aLocal;
            vMaskType = int(aMaskType + 0.5);
            gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        }
    """

    const val BATCH_FRAG = """
        #version 330 core
        in vec2 vUV;
        in vec4 vColor;
        in vec2 vLocal;
        flat in int vMaskType;

        uniform sampler2D uTex;

        out vec4 FragColor;

        void main() {
            if (vMaskType == 1) {
                vec2 p = vLocal * 2.0;
                if (dot(p, p) > 1.0) discard;
            }
            FragColor = texture(uTex, vUV) * vColor;
        }
    """

    const val LINE_BATCH_VERT = """
        #version 330 core
        layout(location = 0) in vec2 aPos;
        layout(location = 1) in vec4 aColor;

        uniform mat4 uProjection;
        out vec4 vColor;

        void main() {
            vColor = aColor;
            gl_Position = uProjection * vec4(aPos, 0.0, 1.0);
        }
    """

    const val LINE_BATCH_FRAG = """
        #version 330 core
        in vec4 vColor;
        out vec4 FragColor;

        void main() {
            FragColor = vColor;
        }
    """
}