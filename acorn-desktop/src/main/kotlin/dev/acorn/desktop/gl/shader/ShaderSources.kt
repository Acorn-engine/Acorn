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
}