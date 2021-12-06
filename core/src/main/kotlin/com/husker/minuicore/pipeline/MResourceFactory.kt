package com.husker.minuicore.pipeline


import com.husker.minuicore.utils.MinUIUtils
import java.io.InputStream
import java.nio.ByteBuffer

abstract class MResourceFactory {

    abstract fun createEmptyTexture(width: Int, height: Int): MTexture
    abstract fun createTexture(width: Int, height: Int, data: ByteBuffer): MTexture

    fun createTexture(width: Int, height: Int, data: ByteArray): MTexture {
        val directByteBuffer = ByteBuffer.allocateDirect(data.size)
        directByteBuffer.put(data)
        MinUIUtils.flipBuffer(directByteBuffer)
        return createTexture(width, height, directByteBuffer)
    }

    fun createTexture(width: Int, height: Int, inputStream: InputStream): MTexture {
        return createTexture(width, height, inputStream.readBytes())
    }

    fun createTextureFromFile(width: Int, height: Int, inputStream: InputStream): MTexture {
        return createTexture(width, height, inputStream.readBytes())
    }
}