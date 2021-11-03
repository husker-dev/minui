package com.husker.minui.natives.impl.win

import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point
import com.husker.minui.natives.PlatformLibrary
import org.lwjgl.glfw.GLFWNativeWin32
import java.nio.charset.StandardCharsets
import java.util.*

object Win: PlatformLibrary("minui-win") {

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

    external fun nGetRegistryValues(hkey: Long, path: String): Array<Any>

    // Implementation

    override fun getFrameDisplay(frame: Frame): Display {
        val hmonitor = nMonitorFromWindow(frame.hwnd)
        val name = nGetMonitorName(hmonitor)
        for(display in Display.displays){
            if(name in GLFWNativeWin32.glfwGetWin32Monitor(display.id)!!)
                return display
        }
        return Display.default
    }

    override fun getMousePosition(): Point {
        val point = nGetMousePosition()
        return Point(point[0].toDouble(), point[1].toDouble())
    }

    override fun screenPointToClient(point: Point, frame: Frame): Point {
        val relativePoint = nScreenToClient(frame.hwnd, point.x.toInt(), point.y.toInt())
        return Point(relativePoint[0].toDouble(), relativePoint[1].toDouble())
    }

    override fun systemBytesToString(bytes: ByteArray): String {
        return if(bytes[0].toInt() == 0)
            bytes.toList().subList(1, bytes.size).toByteArray().utf8Text.toString(StandardCharsets.UTF_8)
        else
            bytes.utf8Text.toString(StandardCharsets.UTF_8)
    }

    override fun getFontPaths(family: String) = WinFonts.getFontPaths(family)

    fun getLCID(locale: Locale): ByteArray = nGetLCID(locale.toString().toByteArray().wideBytes)

    override fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) = WinWindow.setTaskbarIconEnabled(frame, enabled)

    override fun getClipboardDataType() = WinClipboard.getClipboardDataType()
    override fun getClipboardData(type: DataType) = WinClipboard.getClipboardData(type)
    override fun setClipboardData(key: String, bytes: ByteArray) = WinClipboard.setClipboardData(key, bytes)
    override fun setClipboardData(type: DataType, obj: Any) = WinClipboard.setClipboardData(type, obj)
    override fun getClipboardData(key: String) = WinClipboard.getClipboardData(key)
    override fun getClipboardDataKeys() = WinClipboard.getClipboardDataKeys()

    override fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?) = WinPopup.showNativePopup(popup, x, y, frame)


}