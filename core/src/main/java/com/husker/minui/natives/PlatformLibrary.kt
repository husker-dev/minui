package com.husker.minui.natives

import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.OS
import com.husker.minui.core.OS.Companion.Windows
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point
import com.husker.minui.natives.impl.UnknownPlatform
import com.husker.minui.natives.impl.win.Win

abstract class PlatformLibrary(fileName: String): Library(fileName) {

    companion object{
        val instance = when(OS.name){
                Windows -> Win
                else -> UnknownPlatform
            }

        fun isSupported(): Boolean = instance !is UnknownPlatform
    }

    abstract fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean)

    abstract fun getClipboardDataType(): DataType
    abstract fun getClipboardDataKeys(): Array<String>
    abstract fun getClipboardData(key: String): ByteArray?
    abstract fun getClipboardData(type: DataType): Any?
    abstract fun setClipboardData(key: String, bytes: ByteArray)
    abstract fun setClipboardData(type: DataType, obj: Any)

    abstract fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?)

    abstract fun getFrameDisplay(frame: Frame): Display

    abstract fun getMousePosition(): Point
    abstract fun screenPointToClient(point: Point, frame: Frame): Point

    // Example: for Windows - w_string
    abstract fun systemBytesToString(bytes: ByteArray): String

    abstract fun getFontPaths(family: String): List<String>
}