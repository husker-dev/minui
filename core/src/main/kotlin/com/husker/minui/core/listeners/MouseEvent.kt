package com.husker.minui.core.listeners

import com.husker.minui.core.input.MouseAction
import com.husker.minui.geometry.Point
import org.lwjgl.glfw.GLFW.*

class MouseEvent(
    val button: Int,
    val action: MouseAction,
    private val mods: Int,
    val clickCount: Int,
    val position: Point
){

    val time = System.currentTimeMillis()

    val isShiftDown: Boolean
        get() = mods and GLFW_MOD_SHIFT != 0

    val isCtrlDown: Boolean
        get() = mods and GLFW_MOD_CONTROL != 0

    val isAltDown: Boolean
        get() = mods and GLFW_MOD_ALT != 0

    val isSuperKeysDown: Boolean
        get() = mods and GLFW_MOD_SUPER != 0

    val isCapsLocked: Boolean
        get() = mods and GLFW_MOD_CAPS_LOCK != 0

    val isNumLocked: Boolean
        get() = mods and GLFW_MOD_NUM_LOCK != 0
}