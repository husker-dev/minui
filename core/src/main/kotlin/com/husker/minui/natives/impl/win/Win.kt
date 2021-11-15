package com.husker.minui.natives.impl.win

import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.clipboard.ClipboardDataType
import com.husker.minui.core.notification.Notification
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.core.utils.ConcurrentArrayList
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

    // base.h
    external fun nWideTextToMultiByte(bytes: ByteArray): ByteArray
    external fun nMultiByteToWideText(bytes: ByteArray, chars: Int): ByteArray
    external fun nGetMousePositionX(): Int
    external fun nGetMousePositionY(): Int
    external fun nScreenToClientX(hwnd: Long, x: Int, y: Int): Int
    external fun nScreenToClientY(hwnd: Long, x: Int, y: Int): Int
    external fun nGetLCID(localeBytes: ByteArray): ByteArray

    // clipboard.h
    external fun nClipboardGetKeys(): Array<String>
    external fun nClipboardGetData(key: String): ByteArray
    external fun nClipboardEmpty()
    external fun nClipboardSetData(key: String, bytes: ByteArray): ByteArray

    // notification.h
    // Here is temporary empty

    // popup_menu.h
    external fun nPopupCreate(): Long
    external fun nPopupAddString(hmenu: Long, id: Int, wideText: ByteArray)
    external fun nPopupAddSeparator(hmenu: Long)
    external fun nPopupAddSubMenu(hmenu: Long, wideText: ByteArray, subMenu: Long)
    external fun nPopupShow(hmenu: Long, x: Int, y: Int): Int
    external fun nPopupShowWnd(hmenu: Long, x: Int, y: Int, hwnd: Long): Int

    // registry.h
    external fun nRegistryGetMap(hkey: Long, path: ByteArray): Array<Any>

    // toast.h
    external fun nToastInit(id: ByteArray, displayName: ByteArray, imagePath: ByteArray)
    external fun nToastShow(xml: ByteArray)
    external fun nToastUninstall()
    external fun nToastClearAll()

    // window.h
    external fun nGetWindowExStyle(hwnd: Long): Long
    external fun nSetWindowExStyle(hwnd: Long, exStyle: Long)
    external fun nMonitorFromWindow(hwnd: Long): Long
    external fun nGetMonitorName(hwnd: Long): String

    // Callbacks
    val toastCallbackListeners = hashMapOf<Int, ConcurrentArrayList<(Int) -> Unit>>()

    fun onToastCallback(event: String){
        val toastId = event.split("_")[0].toInt()
        if(toastId in toastCallbackListeners)
            toastCallbackListeners[toastId]!!.iterate { it.invoke(event.split("_")[1].toInt()) }
        toastCallbackListeners.remove(toastId)
    }

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
        return Point(nGetMousePositionX().toDouble(), nGetMousePositionY().toDouble())
    }

    override fun screenPointToClient(point: Point, frame: Frame): Point {
        return Point(
            nScreenToClientX(frame.hwnd, point.x.toInt(), point.y.toInt()).toDouble(),
            nScreenToClientY(frame.hwnd, point.x.toInt(), point.y.toInt()).toDouble())
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
    override fun getClipboardData(type: ClipboardDataType) = WinClipboard.getClipboardData(type)
    override fun setClipboardData(key: String, bytes: ByteArray) = WinClipboard.setClipboardData(key, bytes)
    override fun setClipboardData(type: ClipboardDataType, obj: Any) = WinClipboard.setClipboardData(type, obj)
    override fun getClipboardData(key: String) = WinClipboard.getClipboardData(key)
    override fun getClipboardDataKeys() = WinClipboard.getClipboardDataKeys()

    override fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?) = WinPopup.showNativePopup(popup, x, y, frame)

    override fun showNotification(notification: Notification) = WinNotificationExecutor.showNotification(notification)
}