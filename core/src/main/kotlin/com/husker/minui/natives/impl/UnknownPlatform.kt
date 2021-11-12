package com.husker.minui.natives.impl

import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.clipboard.ClipboardDataType
import com.husker.minui.core.notification.Notification
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point
import com.husker.minui.natives.PlatformLibrary
import java.nio.charset.StandardCharsets

object UnknownPlatform: PlatformLibrary("") {
    override fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) {
    }

    override fun getClipboardDataType(): ClipboardDataType = ClipboardDataType.Other
    override fun getClipboardData(type: ClipboardDataType): Any? = null
    override fun setClipboardData(key: String, bytes: ByteArray) {}
    override fun setClipboardData(type: ClipboardDataType, obj: Any) {}
    override fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?) {}
    override fun getFrameDisplay(frame: Frame): Display = Display.default

    override fun getMousePosition(): Point = Point(0.0, 0.0)
    override fun screenPointToClient(point: Point, frame: Frame): Point = Point(-1.0, -1.0)
    override fun systemBytesToString(bytes: ByteArray): String = bytes.toString(StandardCharsets.UTF_8)

    override fun getFontPaths(family: String): List<String> = emptyList()
    override fun showNotification(notification: Notification) {}

    override fun getClipboardDataKeys(): Array<String> = emptyArray()
    override fun getClipboardData(key: String): ByteArray? = null
}