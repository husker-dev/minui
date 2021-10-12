package com.husker.minui.core

import org.lwjgl.glfw.GLFW

class Clipboard {

    var data: String
        get() = GLFW.glfwGetClipboardString(MinUI.frames[0].backend.window).orEmpty()
        set(value) = GLFW.glfwSetClipboardString(MinUI.frames[0].backend.window, value)
}