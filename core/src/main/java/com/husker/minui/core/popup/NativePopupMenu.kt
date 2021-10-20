package com.husker.minui.core.popup

import com.husker.minui.core.Frame
import com.husker.minui.geometry.Point
import com.husker.minui.natives.platform.PlatformLibrary
import kotlin.concurrent.thread

class NativePopupMenu() {

    val elements = arrayListOf<PopupElement>()

    constructor(context: (NativePopupMenu.() -> Unit)?): this(){
        context?.invoke(this)
    }

    fun button(text: String, action: (() -> Unit)? = null){
        elements.add(ButtonElement(text, action))
    }

    fun separator(){
        elements.add(Separator())
    }

    fun subMenu(text: String, context: (NativePopupMenu.() -> Unit)? = null): NativePopupMenu {
        val subMenu = NativePopupMenu(context)
        elements.add(SubMenu(text, subMenu))
        return subMenu
    }

    fun show(point: Point, frame: Frame? = null) = show(point.x.toInt(), point.y.toInt(), frame)

    fun show(x: Int, y: Int, frame: Frame? = null){
        if(PlatformLibrary.isSupported()) PlatformLibrary.instance.showNativePopup(this, x, y, frame)
        else throw UnsupportedOperationException("Native popup are not supported in this OS")
    }

    fun showAsync(x: Int, y: Int, frame: Frame? = null) = thread { show(x, y, frame) }
    fun showAsync(point: Point, frame: Frame? = null) = thread { show(point, frame) }

    interface PopupElement
    class SubMenu(val text: String, val menu: NativePopupMenu): PopupElement
    class ButtonElement(val text: String, val action: (() -> Unit)?): PopupElement
    class Separator: PopupElement
}