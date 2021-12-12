package com.husker.minuicore.pipeline.gl



import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.MResourceFactory
import com.husker.minuicore.pipeline.MTexture

import com.husker.minuicore.utils.Trigger

import java.nio.ByteBuffer
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class GLResourceFactory: MResourceFactory() {

    private var resourcesQueue = LinkedBlockingQueue<(GLResourceFactory) -> Unit>()
    var handle: Long = 0
    lateinit var resourceThread: Thread
    private var contextBound = false

    init{
        MCore.invokeOnMainThreadSync {
            handle = nCreateWindow(0)

            resourceThread = thread(name = "MinUI OpenGL Resources", isDaemon = true){
                requestContext()
                nInit()

                /*
                glMatrixMode(GL_PROJECTION)
                glEnable(GL_TEXTURE_2D)

                glEnable(GL_BLEND)
                glBlendEquation(GL_FUNC_ADD)
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

                 */

                while(!MCore.disposed){
                    val task = resourcesQueue.poll(200, TimeUnit.MILLISECONDS) ?: continue
                    processTask(task)
                }
            }
        }
    }

    fun requestContext(){
        nMakeCurrent(handle)
        contextBound = true
    }

    fun clearContext(){
        nMakeCurrent(0)
        contextBound = false
    }

    private fun processTask(task: (GLResourceFactory) -> Unit){
        task(this)
        if(!contextBound)
            requestContext()
    }

    fun invoke(task: (GLResourceFactory) -> Unit){
        if(Thread.currentThread() == resourceThread)
            processTask(task)
        else
            resourcesQueue.offer(task)
    }

    fun invokeSync(task: (GLResourceFactory) -> Unit){
        if(Thread.currentThread() == resourceThread)
            processTask(task)
        else {
            val trigger = Trigger()
            invoke {
                processTask(task)
                trigger.ready()
            }
            trigger.waitForReady()
        }
    }

    override fun createEmptyTexture(width: Int, height: Int, components: Int) = GLTexture(width, height, components, 0)
    override fun createTexture(width: Int, height: Int, components: Int, pointer: Long) = GLTexture(width, height, components, pointer)
}