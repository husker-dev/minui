package com.husker.minui.natives.platform

import com.husker.minui.core.Frame
import com.husker.minui.core.OS
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.geometry.Point
import com.husker.minui.natives.LibraryUtils

abstract class PlatformLibrary(fileName: String) {

    companion object{
        val instance = when(OS.get()){
                OS.Windows -> Win
                else -> EmptyLibrary
            }

        fun isSupported(): Boolean{
            return instance !is EmptyLibrary
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

    abstract fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?)

    abstract fun getMousePosition(): Point
    abstract fun screenPointToClient(point: Point, frame: Frame): Point
}