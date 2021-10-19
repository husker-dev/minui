package com.husker.minui.core.popup

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

    fun show(point: Point) = show(point.x.toInt(), point.y.toInt())

    fun show(x: Int, y: Int){
        if(PlatformLibrary.isSupported()) PlatformLibrary.instance.showNativePopup(this, x, y)
        else throw UnsupportedOperationException("Native popup are not supported in this OS")
    }

    fun showAsync(x: Int, y: Int) = thread { show(x, y) }
    fun showAsync(point: Point) = thread { show(point) }

    interface PopupElement
    class SubMenu(val text: String, val menu: NativePopupMenu): PopupElement
    class ButtonElement(val text: String, val action: (() -> Unit)?): PopupElement
    class Separator: PopupElement
}