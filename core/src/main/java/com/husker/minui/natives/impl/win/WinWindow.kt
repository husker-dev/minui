package com.husker.minui.natives.impl.win

import com.husker.minui.core.Frame
import com.husker.minui.natives.impl.win.Win.hwnd

class WinWindow { companion object{

    const val WS_EX_NOACTIVATE = 0x08000000L

    // HWND exStyle
    private val cachedExStyles = hashMapOf<Frame, Long>()

    fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) {
        if ((enabled && frame !in cachedExStyles) || (!enabled && frame in cachedExStyles))
            return

        val hwnd = frame.hwnd
        if (enabled) {
            Win.nSetWindowExStyle(hwnd, cachedExStyles.remove(frame)!!) // Reset to default
            frame.visible = false   // TODO: Update window by changing visibility is not the best solution
            frame.visible = true
        } else {
            cachedExStyles[frame] = Win.nGetWindowExStyle(hwnd)
            Win.nSetWindowExStyle(hwnd, WS_EX_NOACTIVATE)
        }
    }
}}