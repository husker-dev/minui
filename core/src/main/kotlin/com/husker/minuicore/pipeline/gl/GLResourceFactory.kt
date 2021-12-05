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

    private var resourcesQueue = LinkedBlockingQueue<(ResourceContext) -> Unit>()
    var handle: Long = 0
    lateinit var resourceThread: Thread

    private val context = ResourceContext(this)   // For tasks

    init{
        MCore.invokeOnMainThreadSync {
            handle = nCreateWindow(0)

            resourceThread = thread(name = "MinUI OpenGL Resources", isDaemon = true){
                requestContext()

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

    private fun requestContext(){
        nMakeCurrent(handle)
        //GL.createCapabilities()
    }

    private fun clearContext(){
        nMakeCurrent(0)
    }

    private fun processTask(task: (ResourceContext) -> Unit){
        task(context)
        if(!context.isContextBound) {
            requestContext()
            context.isContextBound = true
        }
    }

    fun invoke(task: (ResourceContext) -> Unit){
        if(Thread.currentThread() == resourceThread)
            processTask(task)
        else
            resourcesQueue.offer(task)
    }

    fun invokeSync(task: (ResourceContext) -> Unit){
        if(Thread.currentThread() == resourceThread)
            processTask(task)
        else {
            val trigger = Trigger()
            invoke {
                task.invoke(it)
                trigger.ready()
            }
            trigger.waitForReady()
        }
    }

    override fun createTexture(width: Int, height: Int, linear: Boolean): MTexture {
        TODO("Not yet implemented")
    }

    override fun createTexture(width: Int, height: Int, byteBuffer: ByteBuffer, linear: Boolean): MTexture {
        TODO("Not yet implemented")
    }

    class ResourceContext(private val resourceFactory: GLResourceFactory){
        var isContextBound = true

        fun requestContext(){
            isContextBound = true
            resourceFactory.requestContext()
        }

        fun clearContext(){
            isContextBound = false
            resourceFactory.clearContext()
        }
    }

}