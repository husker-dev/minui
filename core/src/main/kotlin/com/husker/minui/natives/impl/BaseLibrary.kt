package com.husker.minui.natives.impl

import com.husker.minui.natives.Library
import java.nio.ByteBuffer

fun String.cStr(): ByteArray{
    val bytes = toByteArray()
    val cBytes = ByteArray(bytes.size + 1)
    System.arraycopy(bytes, 0, cBytes, 0, bytes.size)
    return cBytes
}

object BaseLibrary: Library("minui-base") {

    // freetype.h
    external fun nInitFreetype(): Long
    external fun nLoadFaceFile(ft: Long, path: ByteArray): Long
    external fun nDoneFace(face: Long): Long
    external fun nLoadFace(ft: Long, data: ByteArray): Long
    external fun nSetFaceSize(face: Long, size: Int): Long
    external fun nGetFaceNameCount(face: Long): Int
    external fun nGetFaceName(face: Long, id: Int): Array<Any>
    external fun nFtLoadChar(face: Long, char: Int)
    external fun nFtGetGlyphWidth(face: Long): Long
    external fun nFtGetGlyphHeight(face: Long): Long
    external fun nFtGetGlyphData(face: Long): ByteBuffer

    fun checkInitialization() { /* Here is empty. Used for singleton (object) initialization. */ }


}