package com.husker.minui.natives.platform

import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point

object EmptyLibrary: PlatformLibrary("") {
    override fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) {
    }

    override fun getClipboardDataType(): DataType = DataType.Other
    override fun getClipboardData(type: DataType): Any? = null
    override fun setClipboardData(key: String, bytes: ByteArray) {}
    override fun setClipboardData(type: DataType, obj: Any) {}
    override fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?) {}
    override fun getFrameDisplay(frame: Frame): Display = Display.default

    override fun getMousePosition(): Point = Point(0.0, 0.0)
    override fun screenPointToClient(point: Point, frame: Frame): Point = Point(-1.0, -1.0)

    override fun getClipboardDataKeys(): Array<String> = emptyArray()
    override fun getClipboardData(key: String): ByteArray? = null
}