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

    override fun toString(): String {
        val modsText = arrayListOf<String>()
        if(isShiftDown) modsText.add("shift")
        if(isCtrlDown) modsText.add("ctrl")
        if(isAltDown) modsText.add("alt")
        if(isSuperKeysDown) modsText.add("super")
        if(isCapsLocked) modsText.add("capslock")
        if(isNumLocked) modsText.add("numlock")
        return "KeyEvent(keyCode=$keyCode, scancode=$scancode, action=$action, mods=$modsText)"
    }


}