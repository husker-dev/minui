package com.husker.minui.natives.impl.win

import com.husker.minui.core.clipboard.ClipboardDataType
import com.husker.minui.graphics.Image
import com.husker.minui.natives.impl.win.Win.utf8Text
import com.husker.minui.natives.impl.win.Win.wideBytes
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

class WinClipboard { companion object{

    fun getClipboardDataType(): ClipboardDataType {
        val types = Win.nGetClipboardKeys()
        return if ("FileNameW" in types || "FileGroupDescriptorW" in types) ClipboardDataType.File
        else if ("PNG" in types || "CF_BITMAP" in types) ClipboardDataType.Image
        else if ("CF_UNICODETEXT" in types || "CF_TEXT" in types) ClipboardDataType.Text
        else ClipboardDataType.Other
    }

    fun getClipboardData(type: ClipboardDataType): Any? {
        if (Win.getClipboardDataType() != type)
            return null
        return when (type) {
            ClipboardDataType.File -> {
                val field = if (
                    "FileNameW" !in Win.nGetClipboardKeys() ||
                    Win.nGetClipboardData("FileNameW").utf8Text.toString() == "FileGroupDescriptorW"
                ) "FileGroupDescriptorW" else "FileNameW"
                File(Win.nGetClipboardData(field).utf8Text.toString())
            }
            ClipboardDataType.Text -> {
                if ("CF_UNICODETEXT" in Win.nGetClipboardKeys())
                    Win.nGetClipboardData("CF_UNICODETEXT").utf8Text.toString(StandardCharsets.UTF_8)
                else
                    Win.nGetClipboardData("CF_TEXT").toString(StandardCharsets.US_ASCII)
            }
            ClipboardDataType.Image -> Image(Win.nGetClipboardData("PNG"))
            ClipboardDataType.Other -> null
        }
    }

    fun setClipboardData(key: String, bytes: ByteArray) {
        Win.nSetClipboardData(key, bytes)
    }

    fun setClipboardData(type: ClipboardDataType, obj: Any) {
        when (type) {
            ClipboardDataType.File -> {
                if (obj !is File) throw UnsupportedOperationException("Object is not a File")
                val path = obj.path
                Win.nEmptyClipboard()
                Win.nSetClipboardData("CF_HDROP", path.toByteArray(StandardCharsets.US_ASCII))
            }
            ClipboardDataType.Text -> {
                if (obj !is String) throw UnsupportedOperationException("Object is not a String")
                Win.nEmptyClipboard()
                Win.nSetClipboardData("CF_TEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                Win.nSetClipboardData("CF_OEMTEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                Win.nSetClipboardData("CF_UNICODETEXT", obj.wideBytes)
                Win.nSetClipboardData("CF_LOCALE", Win.getLCID(Locale.getDefault()))
            }
            ClipboardDataType.Image -> TODO()
            ClipboardDataType.Other -> {
                throw UnsupportedOperationException("Can't set \"Other\" data")
            }
        }
    }

    fun getClipboardData(key: String): ByteArray? {
        return if (key in Win.nGetClipboardKeys())
            Win.nGetClipboardData(key)
        else null
    }

    fun getClipboardDataKeys(): Array<String> {
        return Win.nGetClipboardKeys()
    }

}}