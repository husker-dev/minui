package com.husker.minuicore.pipeline.gl.paint

import com.husker.minuicore.pipeline.Graphics
import com.husker.minuicore.pipeline.gl.GLShader

class GLImagePaint: Graphics.Paint() {

    companion object {
        private val imageShader = GLShader.fromResource(
            fragmentPath = "com/husker/minuicore/pipeline/gl/shaders/image/fragment.glsl",
            vertexPath = "com/husker/minuicore/pipeline/gl/shaders/image/vertex.glsl"
        )
    }

    override fun apply() {
        imageShader.use()
    }
}