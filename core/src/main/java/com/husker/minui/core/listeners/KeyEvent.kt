package com.husker.minui.core.listeners

import org.lwjgl.glfw.GLFW.*

data class KeyEvent(
    val keyCode: Int,
    val scancode: Int,
    val action: Action,
    private val mods: Int){

    enum class Action{
        Release,
        Press,
        Type,
        Other
    }

    val time = System.currentTimeMillis()

    val isShiftDown: Boolean
        get() = mods and GLFW_MOD_SHIFT == GLFW_MOD_SHIFT

    val isCtrlDown: Boolean
        get() = mods and GLFW_MOD_CONTROL == GLFW_MOD_CONTROL

    val isAltDown: Boolean
        get() = mods and GLFW_MOD_ALT == GLFW_MOD_ALT

    val isSuperKeysDown: Boolean
        get() = mods and GLFW_MOD_SUPER == GLFW_MOD_SUPER

    val isCapsLocked: Boolean
        get() = mods and GLFW_MOD_CAPS_LOCK == GLFW_MOD_CAPS_LOCK

    val isNumLocked: Boolean
        get() = mods and GLFW_MOD_NUM_LOCK == GLFW_MOD_NUM_LOCK

}