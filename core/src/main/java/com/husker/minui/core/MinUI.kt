package com.husker.minui.core

import com.husker.minui.core.exceptions.GLFWContextException
import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.core.utils.Trigger

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.*

import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

object MinUI {

    const val version = "0.1"

    private var _isActive = true
    val isActive: Boolean
        get() = _isActive

    private var mainQueue = ConcurrentLinkedQueue<Runnable>()
    private var currentWindow = -1L
    private var mainThread: Thread

    val frames = ConcurrentArrayList<Frame>()

    init {
        val initializeTrigger = Trigger()

        mainThread = thread(name = "MinUI Main") {
            GLFWErrorCallback.createPrint(System.err).set()
            if (!glfwInit())
                throw IllegalStateException("Unable to initialize GLFW")

            Resources.checkInitialization()
            initializeTrigger.ready()

            while (isActive) {
                //  Process all runnable than need to be invoked in the main GLFW thread.
                //  They are mainly related to interaction with the UI
                while (mainQueue.isNotEmpty())
                    mainQueue.poll().run()

                // All paint and close actions are processing here
                frames.iterate { frame ->
                    if(!frame.backend.initialized) return@iterate

                    try {
                        makeCurrent(frame.backend.window)
                        frame.draw()
                        with(frame.backend) {
                            if (glfwWindowShouldClose(window)) {
                                if (onClosing()) destroy()
                                else glfwSetWindowShouldClose(window, false)
                            }
                        }
                    }catch (e: GLFWContextException){
                        // TODO: Here is unknown exception
                    }
                }

                glfwPollEvents()
            }

            // Close all not closed windows
            for(frame in frames)
                frame.backend.destroy()


            frames.clear()
        }

        //  Check for active threads.
        //  If there is only one active thread, and it's main MinUI, then shutdown them
        thread(name = "MinUI Thread Checker"){
            while(isActive) {
                val allThreads = Thread.getAllStackTraces().keys
                    .toTypedArray()
                    .filter {
                        it.threadGroup != null && it.threadGroup.name == "main" &&
                        it.name !in arrayOf("DestroyJavaVM", "Monitor Ctrl-Break") &&       // TODO: May not be all names
                        it !in arrayOf(Thread.currentThread(), Resources.thread, mainThread)
                    }

                if (allThreads.isEmpty() && frames.isEmpty())
                    shutdown()
                Thread.sleep(400)
            }
        }

        initializeTrigger.waitForReady()
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
                onKeyAction(key, scancode, action, mods)
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

    fun invokeLaterSync(invokable: () -> Unit) {
        if(Thread.currentThread() == mainThread){
            invokable.invoke()
            return
        }
        val trigger = Trigger()
        invokeLater {
            invokable.invoke()
            trigger.ready()
        }
        trigger.waitForReady()
    }

    fun shutdown(){
        _isActive = false
    }
}