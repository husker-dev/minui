package com.husker.minuicore.pipeline.gl

import com.husker.minuicore.MColor
import com.husker.minuicore.pipeline.Graphics
import com.husker.minuicore.pipeline.MTexture
import com.husker.minuicore.pipeline.gl.paint.GLColorPaint
import com.husker.minuicore.pipeline.gl.paint.GLImagePaint
import com.husker.minuicore.transform.Matrix

class GLGraphics: Graphics() {
    // Default paints
    private val defaultColorPaint by lazy { GLColorPaint(MColor(0.0, 0.0, 0.0, 1.0)) }
    private val defaultImagePaint by lazy { GLImagePaint() }

    private val texCoords = doubleArrayOf(
        1.0, 0.0,
        1.0, 1.0,
        0.0, 0.0,
        0.0, 1.0)

    override var paint: Paint = defaultColorPaint

    private var matrixChanged = false
    private lateinit var allTransforms: Matrix
    private lateinit var ortho: Matrix
    override var transform: Matrix
        get() = super.transform
        set(value) {
            super.transform = value
            allTransforms = ortho * value
            matrixChanged = true
        }

    override var color
        get() = defaultColorPaint.color
        set(value) {
            defaultColorPaint.color = value
            paint = defaultColorPaint
        }

    override fun fillRect(x: Double, y: Double, width: Double, height: Double) {
        paint.apply()
        updateMatrix()
        setAttribute(VERTICES_ATTRIBUTE, 2, doubleArrayOf(
            x + width, y,
            x + width, y + height,
            x, y,
            x, y + height))
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        GLShader.clearShaders()
    }

    override fun drawTexture(texture: MTexture, x: Double, y: Double, width: Double, height: Double) {
        glBindTexture(GL_TEXTURE_2D, (texture as GLTexture).id)
        defaultImagePaint.apply()
        updateMatrix()
        setAttribute(VERTICES_ATTRIBUTE, 2, doubleArrayOf(
            x + width, y,
            x + width, y + height,
            x, y,
            x, y + height)
        )
        setAttribute(TEXTCOORD_ATTRIBUTE, 2, texCoords)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        GLShader.clearShaders()
    }

    private fun updateMatrix() = applyMatrix(allTransforms.elements)

    override fun reset(width: Int, height: Int) {
        ortho = Matrix.ortho(0f, 0f, width.toFloat(), height.toFloat(), 100f, 0.0f)

        GLShader.clearShaders()
        defaultColorPaint.color = MColor(0.0, 0.0, 0.0, 1.0)
        paint = defaultColorPaint

        super.reset(width, height)
    }

}