package com.husker.minui.natives.platform

import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.MinUI
import com.husker.minui.core.OS
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point
import com.husker.minui.graphics.Image
import org.lwjgl.glfw.GLFWNativeWin32
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

object Win: PlatformLibrary("win/${OS.arch}/win.dll") {

    val Frame.hwnd: Long
        get() = GLFWNativeWin32.glfwGetWin32Window(backend.window)

    val String.wideBytes: ByteArray
        get() = toByteArray().wideBytes
    val ByteArray.wideBytes: ByteArray
        get() = nMultiByteToWideText(this, String(this, StandardCharsets.UTF_8).length)
    val ByteArray.utf8Text: ByteArray
        get() = nWideTextToMultiByte(this)

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
    external fun nShowPopupWnd(hmenu: Long, x: Int, y: Int, hwnd: Long): Int

    external fun nGetMousePosition(): IntArray
    external fun nScreenToClient(hwnd: Long, x: Int, y: Int): IntArray

    external fun nGetLCID(localeBytes: ByteArray): ByteArray

    external fun nMonitorFromWindow(hwnd: Long): Long
    external fun nGetMonitorName(hwnd: Long): String

    external fun nWideTextToMultiByte(bytes: ByteArray): ByteArray
    external fun nMultiByteToWideText(bytes: ByteArray, chars: Int): ByteArray

    // ExStyle constants
    private const val WS_EX_NOACTIVATE = 0x08000000L

    // HWND style
    private val cachedExStyles = hashMapOf<Frame, Long>()

    override fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) {
        if ((enabled && frame !in cachedExStyles) || (!enabled && frame in cachedExStyles))
            return

        val hwnd = frame.hwnd
        if (enabled) {
            nSetWindowExStyle(hwnd, cachedExStyles.remove(frame)!!) // Reset to default
            frame.visible = false   // TODO: Update window by changing visibility is not the best solution
            frame.visible = true
        } else {
            cachedExStyles[frame] = nGetWindowExStyle(hwnd)
            nSetWindowExStyle(hwnd, WS_EX_NOACTIVATE)
        }
    }

    // FileGroupDescriptorW
    override fun getClipboardDataType(): DataType {
        val types = nGetClipboardKeys()
        return if ("FileNameW" in types || "FileGroupDescriptorW" in types) DataType.File
        else if ("PNG" in types || "CF_BITMAP" in types) DataType.Image
        else if ("CF_UNICODETEXT" in types || "CF_TEXT" in types) DataType.Text
        else DataType.Other
    }

    override fun getClipboardData(type: DataType): Any? {
        if (getClipboardDataType() != type)
            return null
        return when (type) {
            DataType.File -> {
                val field = if (
                    "FileNameW" !in nGetClipboardKeys() ||
                    nGetClipboardData("FileNameW").utf8Text.toString() == "FileGroupDescriptorW"
                ) "FileGroupDescriptorW" else "FileNameW"
                File(nGetClipboardData(field).utf8Text.toString())
            }
            DataType.Text -> {
                if ("CF_UNICODETEXT" in nGetClipboardKeys())
                    nGetClipboardData("CF_UNICODETEXT").utf8Text.toString(StandardCharsets.UTF_8)
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
        when (type) {
            DataType.File -> {
                if (obj !is File) throw UnsupportedOperationException("Object is not a File")
                val path = obj.path
                nEmptyClipboard()
                nSetClipboardData("CF_HDROP", path.toByteArray(StandardCharsets.US_ASCII))
            }
            DataType.Text -> {
                if (obj !is String) throw UnsupportedOperationException("Object is not a String")
                nEmptyClipboard()
                nSetClipboardData("CF_TEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                nSetClipboardData("CF_OEMTEXT", obj.toByteArray(StandardCharsets.US_ASCII))
                nSetClipboardData("CF_UNICODETEXT", obj.wideBytes)
                nSetClipboardData("CF_LOCALE", getLCID(Locale.getDefault()))
            }
            DataType.Image -> TODO()
            DataType.Other -> {
                throw UnsupportedOperationException("Can't set \"Other\" data")
            }
        }
    }

    override fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?) {
        val indices = arrayListOf<NativePopupMenu.PopupElement>()

        val hmenu = createPopup(popup, indices)
        var result = 0
        if (frame != null)
            MinUI.invokeLaterSync { result = nShowPopupWnd(hmenu, x, y, frame.hwnd) }
        else
            result = nShowPopup(hmenu, x, y)
        if (result != 0 && indices[result - 1] is NativePopupMenu.ButtonElement)
            (indices[result - 1] as NativePopupMenu.ButtonElement).action?.invoke()
    }

    override fun getFrameDisplay(frame: Frame): Display {
        val hmonitor = nMonitorFromWindow(frame.hwnd)
        val name = nGetMonitorName(hmonitor)
        for(display in Display.displays){
            if(name in GLFWNativeWin32.glfwGetWin32Monitor(display.id)!!)
                return display
        }
        return Display.default
    }

    private fun createPopup(popup: NativePopupMenu, indices: ArrayList<NativePopupMenu.PopupElement>): Long {
        val hmenu = nCreatePopupMenu()
        popup.elements.forEach { element ->
            indices.add(element)
            if (element is NativePopupMenu.ButtonElement)
                nAddPopupString(hmenu, indices.size, element.text.wideBytes)
            if (element is NativePopupMenu.Separator)
                nAddPopupSeparator(hmenu)
            if (element is NativePopupMenu.SubMenu)
                nAddPopupSubMenu(hmenu, element.text.wideBytes, createPopup(element.menu, indices))
        }
        return hmenu
    }

    override fun getMousePosition(): Point {
        val point = nGetMousePosition()
        return Point(point[0].toDouble(), point[1].toDouble())
    }

    override fun screenPointToClient(point: Point, frame: Frame): Point {
        val relativePoint = nScreenToClient(frame.hwnd, point.x.toInt(), point.y.toInt())
        return Point(relativePoint[0].toDouble(), relativePoint[1].toDouble())
    }

    fun getLCID(locale: Locale): ByteArray = nGetLCID(locale.toString().toByteArray().wideBytes)

    override fun getClipboardData(key: String): ByteArray? {
        return if (key in nGetClipboardKeys())
            nGetClipboardData(key)
        else null
    }

    override fun getClipboardDataKeys(): Array<String> {
        return nGetClipboardKeys()
    }


}