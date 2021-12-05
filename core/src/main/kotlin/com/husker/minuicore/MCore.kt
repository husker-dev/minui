package com.husker.minuicore

import com.husker.minuicore.pipeline.MPipeline
import com.husker.minuicore.pipeline.MWindow
import com.husker.minuicore.pipeline.gl.GLPipeline
import com.husker.minuicore.platform.MLPlatform
import com.husker.minuicore.platform.win.WinPlatform
import com.husker.minuicore.utils.ConcurrentArrayList
import com.husker.minuicore.utils.MinUIUtils
import com.husker.minuicore.utils.Trigger
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@Suppress("JAVA_CLASS_ON_COMPANION")
class MCore {

    companion object {

        const val version = "0.1"

        var tasksQueue = LinkedBlockingQueue<Runnable>()
        var mainThread: Thread? = null
        var disposed = false
        var forceLibraryLoad = false

        val windows = ConcurrentArrayList<MWindow>()

        val pipeline: MPipeline by lazy {
            // There are only one module for now - OpenGL
            return@lazy GLPipeline()
        }

        val platform: MLPlatform by lazy {
            when(osName){
                "windows" -> WinPlatform()
                //"linux" -> LinuxPlatform()
                //"macos" -> MacPlatform()
                else -> throw UnsupportedOperationException("Unsupported platform")
            }
        }

        val osName by lazy {
            val os = System.getProperty("os.name").lowercase()
            when {
                os.contains("win") -> "windows"
                os.contains("nix") || os.contains("nux") || os.contains("aix") -> "linux"
                os.contains("mac") -> "macos"
                else -> os
            }
        }

        fun loadLibrary(resourcePath: String){
            val target = File(MEnvironment.folder, resourcePath)
            if(forceLibraryLoad || !target.exists()){
                target.parentFile.mkdirs()
                MinUIUtils.copyStreams(javaClass.getResourceAsStream("/$resourcePath")!!, FileOutputStream(target))
            }
            System.load(target.absolutePath)
        }

        fun bindMainThread(newThread: Runnable){
            val thread = Thread.currentThread()
            if(thread.name != "main" || thread.threadGroup.name != "main")
                throw UnsupportedOperationException("Wrapping can be applied only in main thread")
            Thread(newThread).start()
            mainThread = thread
            createDaemonThreadChecker()
            while(!disposed)
                (tasksQueue.poll(150, TimeUnit.MILLISECONDS) ?: continue).run()
        }

        fun invokeOnMainThread(toInvoke: () -> Unit){
            if(mainThread == null)
                throw UnsupportedOperationException("Execution requires thread wrapping. Use this in main thread: wrapMainThread { ... } ")
            if(Thread.currentThread() == mainThread)
                toInvoke.invoke()
            else
                tasksQueue.offer(toInvoke)
        }

        fun <T> invokeOnMainThreadSync(toInvoke: () -> T): T{
            if(mainThread == null)
                throw UnsupportedOperationException("Execution requires thread wrapping. Use this in main thread: wrapMainThread { ... } ")
            if(Thread.currentThread() == mainThread)
                return toInvoke.invoke()
            val trigger = Trigger()
            var value: T? = null
            invokeOnMainThread {
                value = toInvoke.invoke()
                trigger.ready()
            }
            trigger.waitForReady()
            return value!!
        }

        private fun createDaemonThreadChecker(){
            thread(name = "MinUI Daemon Checker", isDaemon = true){
                while(!disposed) {
                    val allThreads = MinUIUtils.getAllThreads()
                        .filter { it != mainThread && !it.isDaemon }
                    if (allThreads.isEmpty() && windows.size == 0) {
                        disposed = true
                        break
                    }
                    Thread.sleep(200)
                }
            }
        }
    }
}