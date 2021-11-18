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
    external fun nFreetypeInit(): Long
    external fun nFreetypeLoadFaceFile(ft: Long, path: ByteArray): Long
    external fun nFreetypeDoneFace(face: Long): Long
    external fun nFreetypeLoadFace(ft: Long, data: ByteArray): Long
    external fun nFreetypeSetFaceSize(face: Long, size: Int): Long
    external fun nFreetypeGetFaceNameCount(face: Long): Int
    external fun nFreetypeGetFaceName(face: Long, id: Int): Array<Any>
    external fun nFreetypeGetCharIndex(face: Long, char: Int): Int
    external fun nFreetypeLoadChar(face: Long, char: Int)
    external fun nFreetypeGetGlyphWidth(face: Long): Long
    external fun nFreetypeGetGlyphHeight(face: Long): Long
    external fun nFreetypeGetGlyphData(face: Long): ByteBuffer
    external fun nFreetypeGetBearingX(face: Long): Int
    external fun nFreetypeGetBearingY(face: Long): Int

    // harfbuzz.h
    external fun nHarfBuzzCreateBuffer(): Long
    external fun nHarfBuzzSetBufferText(buffer: Long, text: ByteArray)
    external fun nHarfBuzzCreateFont(ftFace: Long): Long
    external fun nHarfBuzzShape(font: Long, buffer: Long)
    external fun nHarfBuzzGetGlyphCount(buffer: Long): Int
    external fun nHarfBuzzGetGlyphInfo(buffer: Long): Long
    external fun nHarfBuzzGetGlyphPositions(buffer: Long): Long
    external fun nHarfBuzzGetGlyphId(info: Long, index: Int): Int
    external fun nHarfBuzzGetXOffset(positions: Long, index: Int): Int
    external fun nHarfBuzzGetYOffset(positions: Long, index: Int): Int
    external fun nHarfBuzzGetXAdvance(positions: Long, index: Int): Int
    external fun nHarfBuzzGetYAdvance(positions: Long, index: Int): Int

    fun checkInitialization() { /* Here is empty. Used for singleton (object) initialization. */ }
}