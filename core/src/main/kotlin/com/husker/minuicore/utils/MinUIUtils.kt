package com.husker.minuicore.utils

import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Method
import java.nio.Buffer

class MinUIUtils {
    companion object{
        private var isJava8 = System.getProperty("java.version").startsWith("1.8")

        // Buffer.flip() workaround for Java 1.8
        private var flipMethod: Method? = null
        fun flipBuffer(buffer: Buffer){
            if(isJava8){
                if(flipMethod == null)
                    flipMethod = Buffer::class.java.getMethod("flip")
                flipMethod!!.invoke(buffer)
            }else
                buffer.flip()
        }

        fun copyStreams(input: InputStream, output: OutputStream, buffer: Int = 8192){
            val bufferBytes = ByteArray(buffer)
            var read: Int
            while (input.read(bufferBytes).also { read = it } != -1)
                output.write(bufferBytes, 0, read)

            input.close()
            output.close()
        }

        fun getAllThreads(): Array<Thread>{
            // Get root thread
            var rootGroup = Thread.currentThread().threadGroup
            var parentGroup: ThreadGroup?
            while (rootGroup.parent.also { parentGroup = it } != null)
                rootGroup = parentGroup

            // Fill thread array
            var threads = arrayOfNulls<Thread>(rootGroup.activeCount())
            while (rootGroup.enumerate(threads, true) == threads.size)
                threads = arrayOfNulls(threads.size * 2)

            // Slice until first null
            return threads.sliceArray(0 until threads.indexOfFirst { it == null }).requireNoNulls()
        }
    }
}