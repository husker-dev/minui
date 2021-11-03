package com.husker.minui.natives.impl.win

import com.husker.minui.core.clipboard.DataType
import com.husker.minui.graphics.Image
import com.husker.minui.natives.impl.win.Win.utf8Text
import com.husker.minui.natives.impl.win.Win.wideBytes
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

class WinClipboard { companion object{

    fun getClipboardDataType(): DataType {
        val types = Win.nGetClipboardKeys()
        return if ("FileNameW" in types || "FileGroupDescriptorW" in types) DataType.File
        else if ("PNG" in types || "CF_BITMAP" in types) DataType.Image
        else if ("CF_UNICODETEXT" in types || "CF_TEXT" in types) DataType.Text
        else DataType.Other
    }

    fun getClipboardData(type: DataType): Any? {
        if (Win.getClipboardDataType() != type)
            return null
        return when (type) {
            DataType.File -> {
                val field = if (
                    "FileNameW" !in Win.nGetClipboardKeys() ||
                    Win.nGetClipboardData("FileNameW").utf8Text.toString() == "FileGroupDescriptorW"
                ) "FileGroupDescriptorW" else "FileNameW"
                File(Win.nGetClipboardData(field).utf8Text.toString())
            }
            DataType.Text -> {
                if ("CF_UNICODETEXT" in Win.nGetClipboardKeys())
                    Win.nGetClipboardData("CF_UNICODETEXT").utf8Text.toString(StandardCharsets.UTF_8)
                else
                    Win.nGetClipboardData("CF_TEXT").toString(StandardCharsets.US_ASCII)
            }
            DataType.Image -> Image(Win.nGetClipboardData("PNG"))
            DataType.Other -> null
        }
    }

    fun setClipboardData(key: String, bytes: ByteArray) {
        Win.nSetClipboardData(key, bytes)
    }

    fun setClipboardData(type: DataType, obj: Any) {
        when (type) {
            DataType.File -> {
                if (obj !is File) throw UnsupportedOperationException("Object is not a File")
                val path = obj.path
                Win.nEmptyClipboard()
                Win.nSetClipboardData("CF_HDROP", path.toByteArray(StandardCharsets.US_ASCII))
            }
            DataType.Text -> {
                if (obj !is String) throw UnsupportedOperationException("Object is not a String")
                Win.nEmptyClipboard()
                Win.nSetClipboardData("CF_TEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                Win.nSetClipboardData("CF_OEMTEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                Win.nSetClipboardData("CF_UNICODETEXT", obj.wideBytes)
                Win.nSetClipboardData("CF_LOCALE", Win.getLCID(Locale.getDefault()))
            }
            DataType.Image -> TODO()
            DataType.Other -> {
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