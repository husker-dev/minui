package com.husker.minui.natives.platform

import com.husker.minui.core.Frame
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point
import com.husker.minui.natives.LibraryUtils

abstract class PlatformLibrary(fileName: String) {

    companion object{
        val instance = when(getOS()){
                "windows" -> Win
                else -> EmptyLibrary
            }

        fun isSupported(): Boolean{
            return instance !is EmptyLibrary
        }

        private fun getOS(): String{
            val os = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> "windows"
                os.contains("nix") || os.contains("nux") || os.contains("aix") -> "linux"
                os.contains("mac") -> "macos"
                os.contains("sunos") -> "sunos"
                else -> "unknown"
            }
        }
    }

    init {
        if(fileName.isNotEmpty())
            LibraryUtils.loadResourceLibrary("/natives/libs/$fileName")
    }

    abstract fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean)

    abstract fun getClipboardDataType(): DataType
    abstract fun getClipboardDataKeys(): Array<String>
    abstract fun getClipboardData(key: String): ByteArray?
    abstract fun getClipboardData(type: DataType): Any?
    abstract fun setClipboardData(key: String, bytes: ByteArray)
    abstract fun setClipboardData(type: DataType, obj: Any)

    abstract fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int)

    abstract fun getMousePosition(): Point
    abstract fun screenPointToClient(point: Point, frame: Frame): Point
}