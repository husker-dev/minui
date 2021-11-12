package com.husker.minui.core

import com.husker.minui.core.utils.Trigger
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.glFramebufferTexture
import org.lwjgl.system.MemoryUtil.*
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class Resources { companion object {

    var window: Long = 0
    lateinit var resourceThread: Thread
    private var resourcesQueue = LinkedBlockingQueue<ResourcesRunnable>()

    fun initialize() {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        window = glfwCreateWindow(30, 30, "Resources context", NULL, NULL)

        resourceThread = thread(name = "MinUI Resource"){
            requestContext()

            glMatrixMode(GL_PROJECTION)
            glEnable(GL_TEXTURE_2D)

            while(MinUI.isActive){
                val toInvoke = resourcesQueue.poll(300, TimeUnit.MILLISECONDS) ?: continue
                if(toInvoke.needContext && glfwGetCurrentContext() != window)
                    requestContext()
                if(!toInvoke.needContext)
                    clearContext()
                try {
                    toInvoke.runnable.run()
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun invoke(runnable: Runnable){
        resourcesQueue.offer(ResourcesRunnable(true, runnable))
    }

    fun invokeSync(runnable: Runnable){
        if(this::resourceThread.isInitialized && Thread.currentThread() == resourceThread){
            runnable.run()
            return
        }
        val trigger = Trigger()
        invoke {
            runnable.run()
            trigger.ready()
        }
        trigger.waitForReady()
    }

    fun invokeNoContext(runnable: Runnable){
        resourcesQueue.offer(ResourcesRunnable(false, runnable))
    }

    fun invokeNoContextSync(runnable: Runnable){
        val trigger = Trigger()
        invokeNoContext {
            runnable.run()
            trigger.ready()
        }
        trigger.waitForReady()
    }

    fun readTextureBytes(texId: Int, width: Int, height: Int): ByteBuffer{
        val buffer = ByteBuffer.allocateDirect(width * height * 4)
        invokeSync{
            glBindTexture(GL_TEXTURE_2D, texId)

            val fb = glGenFramebuffers()
            glBindFramebuffer(GL_FRAMEBUFFER, fb)
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texId, 0)

            glReadBuffer(GL_COLOR_ATTACHMENT0)
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer)

            glDeleteFramebuffers(fb)
        }
        return buffer
    }

    fun resizeTexture(texId: Int, newWidth: Int, newHeight: Int, buffer: ByteBuffer, linear: Boolean){
        invokeSync{
            glViewport(0, 0, newWidth, newHeight)
            glLoadIdentity()
            glOrtho(0.0, newWidth.toDouble(), newHeight.toDouble(), 0.0, 0.0, 1.0)

            val fbo = glGenFramebuffers()
            glBindFramebuffer(GL_FRAMEBUFFER, fbo)

            val tex = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, tex)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, newWidth, newHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex, 0)
            glDrawBuffers(GL_COLOR_ATTACHMENT0)

            glBindTexture(GL_TEXTURE_2D, texId)

            val lastFilter = glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER)
            val currentFilter = if(linear) GL_LINEAR else GL_NEAREST
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, currentFilter)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, currentFilter)

            glBegin(GL_QUADS)
            glTexCoord2f(0.0f, 0.0f)
            glVertex2d(0.0, newHeight.toDouble())
            glTexCoord2f(1.0f, 0.0f)
            glVertex2d(newWidth.toDouble(), newHeight.toDouble())
            glTexCoord2f(1.0f, 1.0f)
            glVertex2d(newWidth.toDouble(), 0.0)
            glTexCoord2f(0.0f, 1.0f)
            glVertex2d(0.0, 0.0)
            glEnd()

            glReadBuffer(GL_COLOR_ATTACHMENT0)
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glReadPixels(0, 0, newWidth, newHeight, GL_RGBA, GL_UNSIGNED_BYTE, buffer)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, lastFilter)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, lastFilter)

            glDeleteTextures(tex)
            glDeleteFramebuffers(fbo)
        }
    }

    fun writeTextureBytes(texId: Int, width: Int, height: Int, buffer: ByteBuffer){
        invokeSync{
            glBindTexture(GL_TEXTURE_2D, texId)
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
            glFlush()
        }
    }

    private fun requestContext(){
        glfwMakeContextCurrent(window)
        createCapabilities()
    }

    private fun clearContext(){
        glfwMakeContextCurrent(0)
    }

    private class ResourcesRunnable(val needContext: Boolean, val runnable: Runnable)


}}