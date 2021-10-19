package com.husker.minui.natives.platform

import com.husker.minui.core.Frame
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point
import com.husker.minui.graphics.Image
import org.lwjgl.glfw.GLFWNativeWin32
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

object Win: PlatformLibrary("win.dll") {

    external fun nGetWindowExStyle(hwnd: Long): Long
    external fun nSetWindowExStyle(hwnd: Long, exStyle: Long)

    external fun nGetClipboardKeys(): Array<String>
    external fun nGetClipboardData(key: String): ByteArray
    external fun nSetClipboardData(key: String, bytes: ByteArray): ByteArray
    external fun nEmptyClipboard()

    external fun nCreatePopupMenu(): Long
    external fun nAddPopupString(hmenu: Long, id: Int, wideText: ByteArray)
    external fun nAddPopupSeparator(hmenu: Long)
    external fun nAddPopupSubMenu(hmenu: Long, wideText: ByteArray, subMenu: Long)
    external fun nShowPopup(hmenu: Long, x: Int, y: Int): Int

    external fun nGetMousePosition(): IntArray
    external fun nScreenToClient(hwnd: Long, x: Int, y: Int): IntArray

    external fun nGetLCID(localeBytes: ByteArray): Int

    external fun nWideTextToMultiByte(bytes: ByteArray): ByteArray
    external fun nMultiByteToWideText(bytes: ByteArray, chars: Int): ByteArray

    // ExStyle constants
    private const val WS_EX_NOACTIVATE = 0x08000000L

    // HWND style
    private val cachedExStyles = hashMapOf<Frame, Long>()

    override fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) {
        if((enabled && frame !in cachedExStyles) || (!enabled && frame in cachedExStyles))
            return

        val hwnd = GLFWNativeWin32.glfwGetWin32Window(frame.backend.window)
        if(enabled){
            nSetWindowExStyle(hwnd, cachedExStyles.remove(frame)!!) // Reset to default
            frame.visible = false   // TODO: Update window by changing visibility is not the best solution
            frame.visible = true
        }else{
            cachedExStyles[frame] = nGetWindowExStyle(hwnd)
            nSetWindowExStyle(hwnd, WS_EX_NOACTIVATE)
        }
    }

    // FileGroupDescriptorW
    override fun getClipboardDataType(): DataType {
        val types = nGetClipboardKeys()
        return if("FileNameW" in types || "FileGroupDescriptorW" in types)  DataType.File
        else if("PNG" in types || "CF_BITMAP" in types)                     DataType.Image
        else if("CF_UNICODETEXT" in types || "CF_TEXT" in types)            DataType.Text
        else DataType.Other
    }

    override fun getClipboardData(type: DataType): Any? {
        if(getClipboardDataType() != type)
            return null
        return when(type){
            DataType.File -> {
                val field = if(
                    "FileNameW" !in nGetClipboardKeys() ||
                    nWideTextToMultiByte(nGetClipboardData("FileNameW")).toString() == "FileGroupDescriptorW"
                ) "FileGroupDescriptorW" else "FileNameW"
                File(nWideTextToMultiByte(nGetClipboardData(field)).toString(StandardCharsets.UTF_8))
            }
            DataType.Text -> {
                if("CF_UNICODETEXT" in nGetClipboardKeys())
                    nWideTextToMultiByte(nGetClipboardData("CF_UNICODETEXT")).toString(StandardCharsets.UTF_8)
                else
                    nGetClipboardData("CF_TEXT").toString(StandardCharsets.US_ASCII)
            }
            DataType.Image -> Image(nGetClipboardData("PNG"))
            DataType.Other -> null
        }
    }

    override fun setClipboardData(key: String, bytes: ByteArray) {
        nSetClipboardData(key, bytes)
    }

    override fun setClipboardData(type: DataType, obj: Any) {
        when(type){
            DataType.File -> {
                if(obj !is File) throw UnsupportedOperationException("Object is not a File")
                val path = obj.path
                nEmptyClipboard()
                nSetClipboardData("CF_HDROP", path.toByteArray(StandardCharsets.US_ASCII))
            }
            DataType.Text -> {
                if(obj !is String) throw UnsupportedOperationException("Object is not a String")
                nEmptyClipboard()
                nSetClipboardData("CF_TEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                nSetClipboardData("CF_OEMTEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                nSetClipboardData("CF_UNICODETEXT", nMultiByteToWideText(obj.toByteArray(StandardCharsets.UTF_8), obj.length))
                nSetClipboardData("CF_LOCALE", getLCID())
            }
            DataType.Image -> TODO()
            DataType.Other -> {
                throw UnsupportedOperationException("Can't set \"Other\" data")
            }
        }
    }

    override fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int) {
        val indices = arrayListOf<NativePopupMenu.PopupElement>()

        val result = nShowPopup(createPopup(popup, indices), x, y)
        if(result != 0 && indices[result - 1] is NativePopupMenu.ButtonElement)
            (indices[result - 1] as NativePopupMenu.ButtonElement).action?.invoke()
    }

    private fun createPopup(popup: NativePopupMenu, indices: ArrayList<NativePopupMenu.PopupElement>): Long{
        val hmenu = nCreatePopupMenu()
        popup.elements.forEach{ element ->
            indices.add(element)
            if(element is NativePopupMenu.ButtonElement)
                nAddPopupString(hmenu, indices.size, nMultiByteToWideText(element.text.toByteArray(), element.text.length))
            if(element is NativePopupMenu.Separator)
                nAddPopupSeparator(hmenu)
            if(element is NativePopupMenu.SubMenu)
                nAddPopupSubMenu(hmenu, nMultiByteToWideText(element.text.toByteArray(), element.text.length), createPopup(element.menu, indices))
        }
        return hmenu
    }

    override fun getMousePosition(): Point {
        val point = nGetMousePosition()
        return Point(point[0].toDouble(), point[1].toDouble())
    }

    override fun screenPointToClient(point: Point, frame: Frame): Point {
        val relativePoint = nScreenToClient(GLFWNativeWin32.glfwGetWin32Window(frame.backend.window), point.x.toInt(), point.y.toInt())
        return Point(relativePoint[0].toDouble(), relativePoint[1].toDouble())
    }

    fun getLCID(): ByteArray {
        val localeString = Locale.getDefault().toString()
        return byteArrayOf(nGetLCID(nMultiByteToWideText(localeString.toByteArray(), localeString.length)).toByte())
    }

    override fun getClipboardData(key: String): ByteArray? {
        return if(key in nGetClipboardKeys())
            nGetClipboardData(key)
        else null
    }

    override fun getClipboardDataKeys(): Array<String> {
        return nGetClipboardKeys()
    }


}