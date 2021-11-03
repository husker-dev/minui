package com.husker.minui.natives.impl.win

import com.husker.minui.core.Frame
import com.husker.minui.core.MinUI
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.natives.impl.win.Win.hwnd
import com.husker.minui.natives.impl.win.Win.wideBytes
import java.util.ArrayList

class WinPopup{ companion object{
    fun showNativePopup(popup: NativePopupMenu, x: Int, y: Int, frame: Frame?) {
        val indices = arrayListOf<NativePopupMenu.PopupElement>()

        val hmenu = createPopup(popup, indices)
        var result = 0
        if (frame != null)
            MinUI.invokeLaterSync { result = Win.nShowPopupWnd(hmenu, x, y, frame.hwnd) }
        else
            result = Win.nShowPopup(hmenu, x, y)
        if (result != 0 && indices[result - 1] is NativePopupMenu.ButtonElement)
            (indices[result - 1] as NativePopupMenu.ButtonElement).action?.invoke()
    }

    private fun createPopup(popup: NativePopupMenu, indices: ArrayList<NativePopupMenu.PopupElement>): Long {
        val hmenu = Win.nCreatePopupMenu()
        popup.elements.forEach { element ->
            indices.add(element)
            if (element is NativePopupMenu.ButtonElement)
                Win.nAddPopupString(hmenu, indices.size, element.text.wideBytes)
            if (element is NativePopupMenu.Separator)
                Win.nAddPopupSeparator(hmenu)
            if (element is NativePopupMenu.SubMenu)
                Win.nAddPopupSubMenu(hmenu, element.text.wideBytes, createPopup(element.menu, indices))
        }
        return hmenu
    }
}}


