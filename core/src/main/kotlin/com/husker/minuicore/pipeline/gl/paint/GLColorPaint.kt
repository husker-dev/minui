package com.husker.minuicore.pipeline.gl.paint

import com.husker.minuicore.MColor
import com.husker.minuicore.pipeline.Graphics
import com.husker.minuicore.pipeline.gl.GLShader

class GLColorPaint(var color: MColor): Graphics.Paint() {

    companion object {
        private val colorShader = GLShader.fromResource(
            fragmentPath = "com/husker/minuicore/pipeline/gl/shaders/color/fragment.glsl",
            vertexPath = "com/husker/minuicore/pipeline/gl/shaders/color/vertex.glsl",
        )
    }

    override fun apply() {
        colorShader.use()
        colorShader.setVariable("color", color.red.toFloat(), color.green.toFloat(), color.blue.toFloat(), color.alpha.toFloat())
    }

}