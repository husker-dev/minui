package com.husker.minuicore.pipelines.gl

import com.husker.minui.MinUI
import com.husker.minuicore.MLCore
import com.husker.minuicore.MLPipeline
import com.husker.minuicore.MLWindow
import com.husker.minuicore.utils.Trigger
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.*
import java.util.concurrent.TimeUnit

class GLPipeline: MLPipeline("OpenGL") {

    private var currentContext = Long.MIN_VALUE

    override val resourceFactory = GLResourceFactory()

    init{
        val initializeTrigger = Trigger()

        MLCore.invokeOnMainThread {
            GLFWErrorCallback.createPrint(System.err).set()
            if (!glfwInit())
                throw IllegalStateException("Unable to initialize GLFW")

            initializeTrigger.ready()
            while(!MLCore.disposed){
                // Checking for task to execute in main thread
                if(MLCore.tasksQueue.size > 0)
                    MLCore.tasksQueue.poll(1, TimeUnit.MILLISECONDS)!!.run()

                // Redraw every window
                MinUI.frames.iterate { frame ->
                    if(!frame.backend.initialized) return@iterate

                    MinUI.makeCurrent(frame.backend.window)
                    //frame.backend.drawGL()
                    with(frame.backend) {
                        if (glfwWindowShouldClose(window)) {
                            if (onClosing()) destroy()
                            else glfwSetWindowShouldClose(window, false)
                        }
                    }
                }
                glfwPollEvents()
            }
        }
        initializeTrigger.waitForReady()
    }

    override fun createWindow(): MLWindow {
        return GLWindow()
    }

    fun makeCurrentContext(window: Long){
        if(window != currentContext){
            currentContext = window
            glfwMakeContextCurrent(window)
            createCapabilities()
        }
    }

}