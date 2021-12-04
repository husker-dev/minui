package com.husker.minuicore.pipeline


import com.husker.minuicore.utils.MinUIUtils
import java.io.InputStream
import java.nio.ByteBuffer

abstract class MLResourceFactory {

    abstract fun createTexture(width: Int, height: Int, linear: Boolean): MLTexture
    abstract fun createTexture(width: Int, height: Int, byteBuffer: ByteBuffer, linear: Boolean): MLTexture

    fun createTexture(width: Int, height: Int, byteArray: ByteArray, linear: Boolean): MLTexture {
        val directByteBuffer = ByteBuffer.allocateDirect(byteArray.size)
        directByteBuffer.put(byteArray)
        MinUIUtils.flipBuffer(directByteBuffer)
        return createTexture(width, height, directByteBuffer, linear)
    }

    fun createTexture(width: Int, height: Int, inputStream: InputStream, linear: Boolean): MLTexture {
        return createTexture(width, height, inputStream.readBytes(), linear)
    }
}