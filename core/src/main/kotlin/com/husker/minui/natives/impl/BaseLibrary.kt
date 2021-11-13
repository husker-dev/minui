package com.husker.minui.natives.impl

import com.husker.minui.natives.Library
import java.io.File
import java.nio.charset.StandardCharsets

object BaseLibrary: Library("minui-base") {

    // freetype.h
    external fun nInitFreetype(): Long
    external fun nLoadFaceFile(ft: Long, path: String): Long
    external fun nDoneFace(face: Long): Long
    external fun nLoadFace(ft: Long, data: ByteArray): Long
    external fun nSetFaceSize(face: Long, size: Int)
    external fun nGetFaceNameCount(face: Long): Int
    external fun nGetFaceName(face: Long, id: Int): Array<Any>

    val nFreetype: Long = nInitFreetype()

    fun checkInitialization() { /* Here is empty. Used for singleton (object) initialization. */ }

    fun loadFont(data: ByteArray): Long{
        val result = nLoadFace(nFreetype, data)
        if(result == -1L)
            throw UnsupportedOperationException("Failed to load font")
        return result
    }

    fun loadFont(path: String): Long{
        if(!File(path).exists())
            throw NullPointerException("File doesn't exist: $path")
        val result = nLoadFaceFile(nFreetype, path)
        if(result == -1L)
            throw UnsupportedOperationException("Failed to load font: $path")
        return result
    }

    fun doneFont(face: Long){
        if(nDoneFace(face) == -1L)
            throw UnsupportedOperationException("Failed to done face: $face")
    }

    fun setFontSize(face: Long, size: Int) = nSetFaceSize(face, size)

    fun getFontMetaCount(face: Long) = nGetFaceNameCount(face)

    fun getFontMeta(face: Long, id: Int): String {
        val result = nGetFaceName(face, id)
        val bytes = result[0] as ByteArray
        val encoding = result[1] as IntArray

        // - Windows (BMP)
        // - Standard Unicode (2.0)
        return if(
            (encoding[0] == 3 && encoding[1] == 1) ||
            (encoding[0] == 0 && encoding[1] == 3))
            bytes.toString(StandardCharsets.UTF_16BE)
        else
            bytes.toString(StandardCharsets.UTF_8)
    }

}