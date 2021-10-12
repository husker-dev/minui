package com.husker.minui.natives

import com.husker.minui.core.Frame
import org.lwjgl.glfw.GLFWNativeWin32

object Win: PlatformLibrary("win.dll") {

    private external fun getWindowExStyle(hwnd: Long): Long
    private external fun setWindowExStyle(hwnd: Long, exStyle: Long)
    private external fun updateWindow(hwnd: Long)

    private val cachedExStyles = hashMapOf<Frame, Long>()

    override fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) {
        if((enabled && frame !in cachedExStyles) || (!enabled && frame in cachedExStyles))
            return

        val hwnd = GLFWNativeWin32.glfwGetWin32Window(frame.backend.window)
        if(enabled){
            val currentStyle = cachedExStyles[frame]!!
            cachedExStyles.remove(frame)
            setWindowExStyle(hwnd, currentStyle) // Reset to default
            frame.visible = false   // TODO: Update window by changing visibility is not the best solution
            frame.visible = true
        }else{
            val currentStyle = getWindowExStyle(hwnd)
            cachedExStyles[frame] = currentStyle
            setWindowExStyle(hwnd, 0x08000000L) // Set WS_EX_NOACTIVATE
        }

    }
}