package com.husker.minuicore.pipeline.gl

import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.Graphics
import com.husker.minuicore.pipeline.MTexture

class GLGraphics: Graphics() {
    private val factory = MCore.pipeline.resourceFactory as GLResourceFactory

    override fun fillRect(x: Double, y: Double, width: Double, height: Double) {
        factory.colorShader.use()

        

        GLShader.clearShaders()
    }

    override fun drawTexture(texture: MTexture, x: Double, y: Double, width: Double, height: Double) {

    }

}