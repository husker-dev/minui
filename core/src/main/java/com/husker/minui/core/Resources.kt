package com.husker.minui.core

import com.husker.minui.core.utils.Trigger
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.*
import java.nio.ByteBuffer
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
        window = glfwCreateWindow(800, 800, "Resources context", NULL, NULL)

        resourceThread = thread(name = "MinUI Resource"){
            requestContext()
            glEnable(GL_TEXTURE_2D)

            while(MinUI.isActive){
                val toInvoke = resourcesQueue.poll(300, TimeUnit.MILLISECONDS) ?: continue
                if(toInvoke.needContext && glfwGetCurrentContext() != window)
                    requestContext()
                if(!toInvoke.needContext)
                    clearContext()
                toInvoke.runnable.run()
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
            glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer)

            glDeleteFramebuffers(fb)
        }
        return buffer
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