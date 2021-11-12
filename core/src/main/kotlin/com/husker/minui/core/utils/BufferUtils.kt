package com.husker.minui.core.utils

import java.lang.reflect.Method
import java.nio.Buffer
import java.nio.ByteBuffer

class BufferUtils {

    companion object{

        // Buffer.flip() workaround for Java 1.8
        private var isJava8 = System.getProperty("java.version").startsWith("1.8")
        private var flipMethod: Method? = null
        fun flipBuffer(buffer: ByteBuffer){
            if(isJava8){
                if(flipMethod == null)
                    flipMethod = Buffer::class.java.getMethod("flip")
                flipMethod!!.invoke(buffer)
            }else
                buffer.flip()
        }
    }
}