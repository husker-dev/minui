package com.husker.minuicore.pipeline


import com.husker.minuicore.MCore
import java.io.InputStream
import java.net.URL

abstract class MResourceFactory {

    abstract fun createEmptyTexture(width: Int, height: Int, components: Int): MTexture
    abstract fun createTexture(width: Int, height: Int, components: Int, pointer: Long): MTexture

    fun createTextureFromByteArray(imageFileData: ByteArray): MTexture {
        val info = MCore.nLoadImageFromBytes(imageFileData)
        return createTexture(info[1].toInt(), info[2].toInt(), info[3].toInt(), info[0])
    }

    fun createTextureFromFile(path: String): MTexture {
        val info = MCore.nLoadImageFromFile(path)
        return createTexture(info[1].toInt(), info[2].toInt(), info[3].toInt(), info[0])
    }

    fun createTextureFromURL(imageURL: URL) = createTextureFromStream(imageURL.openStream())
    fun createTextureFromURL(imageURL: String) = createTextureFromURL(URL(imageURL))
    fun createTextureFromStream(inputStream: InputStream) = createTextureFromByteArray(inputStream.readBytes())

}