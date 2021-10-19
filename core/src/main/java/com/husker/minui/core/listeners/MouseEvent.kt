package com.husker.minui.core.listeners

import com.husker.minui.geometry.Point
import org.lwjgl.glfw.GLFW

class MouseEvent(
    val button: Int,
    val action: Action,
    private val mods: Int,
    val clickCount: Int,
    val position: Point
){

    enum class Action{
        Release,
        Press,
        Click
    }

    val time = System.currentTimeMillis()

    val isShiftDown: Boolean
        get() = mods and GLFW.GLFW_MOD_SHIFT == GLFW.GLFW_MOD_SHIFT

    val isCtrlDown: Boolean
        get() = mods and GLFW.GLFW_MOD_CONTROL == GLFW.GLFW_MOD_CONTROL

    val isAltDown: Boolean
        get() = mods and GLFW.GLFW_MOD_ALT == GLFW.GLFW_MOD_ALT

    val isSuperKeysDown: Boolean
        get() = mods and GLFW.GLFW_MOD_SUPER == GLFW.GLFW_MOD_SUPER

    val isCapsLocked: Boolean
        get() = mods and GLFW.GLFW_MOD_CAPS_LOCK == GLFW.GLFW_MOD_CAPS_LOCK

    val isNumLocked: Boolean
        get() = mods and GLFW.GLFW_MOD_NUM_LOCK == GLFW.GLFW_MOD_NUM_LOCK
}