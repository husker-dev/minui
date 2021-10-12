package com.husker.minui.core

import com.husker.minui.core.utils.Trigger
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

object Resources {

    var window: Long = 0
    var thread: Thread
    private var resourcesQueue = LinkedBlockingQueue<ResourcesRunnable>()

    init{
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        window = glfwCreateWindow(800, 800, "Resources context", NULL, NULL)

        thread = thread(name = "MinUI Resource"){
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

    fun checkInitialization() { /* Here is empty. Used for singleton (object) initialization. */ }

    fun invoke(runnable: Runnable){
        resourcesQueue.offer(ResourcesRunnable(true, runnable))
    }

    fun invokeSync(runnable: Runnable){
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

    private fun requestContext(){
        glfwMakeContextCurrent(window)
        createCapabilities()
    }

    private fun clearContext(){
        glfwMakeContextCurrent(0)
    }

    private class ResourcesRunnable(val needContext: Boolean, val runnable: Runnable)


}