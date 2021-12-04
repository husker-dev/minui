package com.husker.minui.core.gl

import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import kotlin.math.round

class GLRenders { companion object {

    val textShader: Shader by lazy{
        Shader(fragment = this::class.java.getResourceAsStream("/com/husker/minui/gl/shaders/textShader.fs")!!.readBytes().toString())
    }

    fun createTexture(width: Int, height: Int, type: Int, linear: Boolean = false): Int{
        val textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if(linear) GL_LINEAR else GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, if(linear) GL_LINEAR else GL_NEAREST)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexImage2D(GL_TEXTURE_2D, 0, type, width, height, 0, type, GL_UNSIGNED_BYTE, 0)
        return textureId
    }

    fun createTexture(width: Int, height: Int, type: Int, linear: Boolean = false, data: ByteBuffer): Int{
        val textureId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if(linear) GL_LINEAR else GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, if(linear) GL_LINEAR else GL_NEAREST)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, type, GL_UNSIGNED_BYTE, data)
        return textureId
    }

    fun drawRectangle(x: Float, y: Float, width: Float, height: Float, textureId: Int, flip: Boolean = false){
        MemoryStack.stackPush().use { stack ->
            val x = round(x)
            val y = round(y)
            val width = round(width)
            val height = round(height)

            val vertices = stack.floats(
                x + width, y,
                x + width, y + height,
                x, y,
                x, y + height
            )

            val texVertices = if(flip)
                stack.floats(
                    1f, 1f,
                    1f, 0f,
                    0f, 1f,
                    0f, 0f
                )
            else
                stack.floats(
                    1f, 0f,
                    1f, 1f,
                    0f, 0f,
                    0f, 1f
                )

            glEnableClientState(GL_VERTEX_ARRAY)
            glEnableClientState(GL_TEXTURE_COORD_ARRAY)

            glBindTexture(GL_TEXTURE_2D, textureId)
            glVertexPointer(2, GL_FLOAT, 0, vertices)
            glTexCoordPointer(2, GL_FLOAT, 0, texVertices)
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)

            glDisableClientState(GL_VERTEX_ARRAY)
            glDisableClientState(GL_TEXTURE_COORD_ARRAY)
        }
    }

    fun renderOnTexture(textureId: Int, render: () -> Unit){
        glBindTexture(GL_TEXTURE_2D, textureId)
        val width = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH)
        val height = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT)

        glViewport(0, 0, width, height)
        glLoadIdentity()
        glOrtho(0.0, width.toDouble(), height.toDouble(), 0.0, 0.0, 1.0)

        val fbo = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, fbo)

        GL32.glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, textureId, 0)
        glDrawBuffers(GL_COLOR_ATTACHMENT0)
        glBindFramebuffer(GL_FRAMEBUFFER, fbo)

        render.invoke()
        glDeleteFramebuffers(fbo)
    }
} }