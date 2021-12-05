package com.husker.minuicore.pipeline.gl


import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.MPipeline
import com.husker.minuicore.pipeline.MWindow

import java.util.concurrent.TimeUnit

external fun nCreateWindow(shareWith: Long): Long
external fun nSwapBuffers(handle: Long)
external fun nPollEvents()
external fun nMakeCurrent(handle: Long)
external fun nSetVsync(handle: Long, value: Boolean)
external fun nGetVsync(handle: Long): Boolean

class GLPipeline: MPipeline("OpenGL") {

    companion object {
        init{
            MCore.loadLibrary("minui_natives/${MCore.osName}/${MCore.platform.architecture}/gl.dll")
        }
    }

    private var currentContext = Long.MIN_VALUE

    override val resourceFactory by lazy { GLResourceFactory() }

    init{
        MCore.invokeOnMainThread {
            while(!MCore.disposed){
                // Checking for task to execute in main thread
                if(MCore.tasksQueue.size > 0)
                    MCore.tasksQueue.poll(1, TimeUnit.MILLISECONDS)!!.run()

                // Redraw every window
                MCore.windows.iterate { window ->
                    window as GLWindow
                    if(!window.initialized) return@iterate

                    makeCurrentContext(window.handle)
                    window.render()
                }
                if(MCore.windows.size > 0)
                    nPollEvents()
            }
        }
    }

    override fun createWindow(): MWindow {
        return GLWindow()
    }

    fun makeCurrentContext(window: Long){
        if(currentContext != window){
            currentContext = window
            nMakeCurrent(window)
        }
    }

}