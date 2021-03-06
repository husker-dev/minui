package com.husker.minuicore.pipeline.gl

import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.MTexture

class GLTexture(): MTexture() {

    override var width = 0
    override var height = 0
    var dataPointer: Long = 0
    var id = 0
    override var components = 3

    override var linear: Boolean
        get() {
            var result = false
            resourceThread {
                glBindTexture(GL_TEXTURE_2D, id)
                result = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_MIN_FILTER) == GL_LINEAR
            }
            return result
        }
        set(value) {
            resourceThread {
                glBindTexture(GL_TEXTURE_2D, id)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if(value) GL_LINEAR else GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, if(value) GL_LINEAR else GL_NEAREST)
            }
        }

    constructor(width: Int, height: Int, components: Int, dataPointer: Long): this(){
        this.width = width
        this.height = height
        this.dataPointer = dataPointer
        this.components = components
        init()
    }

    constructor(id: Int): this(){
        resourceThread {
            glBindTexture(GL_TEXTURE_2D, id)
            width = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH)
            height = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT)
        }
    }

    private fun init(){
        resourceThread {
            if(id == 0)
                id = glGenTextures()

            glBindTexture(GL_TEXTURE_2D, id)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexImage2D(GL_TEXTURE_2D, 0, if(components == 3) GL_RGB else GL_RGBA, width, height, 0, if(components == 3) GL_RGB else GL_RGBA, GL_UNSIGNED_BYTE, dataPointer)
            glFlush()
        }
    }

    private fun resourceThread(run: () -> Unit){
        (MCore.pipeline.resourceFactory as GLResourceFactory).invokeSync { run.invoke() }
    }
}