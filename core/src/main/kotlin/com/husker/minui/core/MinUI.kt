package com.husker.minui.core

import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.core.utils.Trigger
import com.husker.minui.graphics.Image
import com.husker.minui.natives.impl.BaseLibrary

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.*
import org.lwjgl.system.Configuration

import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

class MinUI { companion object {

    var appName = "MinUI Application"
    var appId = "minui.application"
    var appIcon: Image? = null

    const val version = "0.1"

    private var _initialized = false
    val initialized: Boolean
        get() = _initialized

    private var _isActive = true
    val isActive: Boolean
        get() = _isActive

    private var mainQueue = ConcurrentLinkedQueue<Runnable>()
    private var currentWindow = -1L
    private lateinit var mainThread: Thread

    val frames = ConcurrentArrayList<Frame>()

    fun initialize(){
        if(initialized) return
        _initialized = true

        BaseLibrary.checkInitialization()
        //Configuration.DISABLE_CHECKS.set(true)

        val initializeTrigger = Trigger()

        mainThread = thread(name = "MinUI Main") {
            GLFWErrorCallback.createPrint(System.err).set()
            if (!glfwInit())
                throw IllegalStateException("Unable to initialize GLFW")
            Resources.initialize()

            // The main initialization is completed, then there is a loop with windows
            initializeTrigger.ready()

            while (isActive) {
                //  Process all runnable than need to be invoked in the main GLFW thread.
                //  They are mainly related to interaction with the UI
                while (mainQueue.isNotEmpty())
                    mainQueue.poll().run()

                // All paint and close actions are processing here
                frames.iterate { frame ->
                    if(!frame.backend.initialized) return@iterate

                    makeCurrent(frame.backend.window)
                    frame.backend.drawGL()
                    with(frame.backend) {
                        if (glfwWindowShouldClose(window)) {
                            if (onClosing()) destroy()
                            else glfwSetWindowShouldClose(window, false)
                        }
                    }

                }
                glfwPollEvents()
            }

            // Close all not closed windows
            for(frame in frames)
                frame.backend.destroy()

            frames.clear()
        }

        initializeTrigger.waitForReady()

        //  Checking for active threads.
        //  If there are only MinUI threads, then shutdown them
        thread(name = "MinUI Background"){
            while(isActive) {
                val allThreads = Thread.getAllStackTraces().keys
                    .toTypedArray()
                    .filter {
                        it.threadGroup != null && it.threadGroup.name == "main" &&
                                it.name !in arrayOf("DestroyJavaVM", "Monitor Ctrl-Break") &&       // TODO: May not be all names
                                it != Thread.currentThread() &&
                                it != Resources.resourceThread &&
                                it != mainThread
                    }

                if (allThreads.isEmpty() && frames.isEmpty())
                    shutdown()
                Thread.sleep(400)
            }
        }
    }

    fun makeCurrent(window: Long){
        if(window != currentWindow){
            currentWindow = window
            glfwMakeContextCurrent(window)
            createCapabilities()
        }
    }

    fun initializeCallbacks(frame: Frame){
        with(frame.backend){
            glfwSetFramebufferSizeCallback(window){ _, width, height ->
                makeCurrent(window)
                onResize(width, height)
            }

            glfwSetWindowPosCallback(window) { _, x, y ->
                makeCurrent(window)
                onMove(x, y)
            }

            glfwSetKeyCallback(window) { _, key, scancode, action, mods ->
                makeCurrent(window)
                onKeyAction(key, scancode, action, mods)
            }

            glfwSetCursorPosCallback(window){ _, _, _ ->
                makeCurrent(window)
                onMouseMove()
            }

            glfwSetMouseButtonCallback(window){ _, button, action, mods ->
                makeCurrent(window)
                onMouseAction(button, action, mods)
            }

            glfwSetWindowMaximizeCallback(window){ _, maximized ->
                makeCurrent(window)
                onMaximize(maximized)
            }
            glfwSetWindowIconifyCallback(window){ _, iconified ->
                makeCurrent(window)
                onIconify(iconified)
            }
        }
    }

    fun checkInitialization() { /* Here is empty. Used for singleton (object) initialization. */ }

    fun invokeLater(invokable: () -> Unit) {
        if(Thread.currentThread() == mainThread){
            invokable.invoke()
            return
        }
        mainQueue.offer(invokable)
    }

    fun <T> invokeLaterSync(invokable: () -> T): T? {
        if(Thread.currentThread() == mainThread)
            return invokable.invoke()

        val trigger = Trigger()
        var result: T? = null
        invokeLater {
            result = invokable.invoke()
            trigger.ready()
        }
        trigger.waitForReady()
        return result
    }

    fun shutdown(){
        _isActive = false
    }
}}