package com.husker.minui.core.input

import com.husker.minui.core.Frame
import com.husker.minui.geometry.Point
import com.husker.minui.natives.PlatformLibrary

class MouseButton { companion object {
    const val left = 0
    const val right = 1
    const val middle = 2
    const val previous = 3
    const val next = 4
} }

enum class MouseAction{
    Release,
    Press,
    Click
}

class Mouse { companion object {

    val location: Point
        get() = if (PlatformLibrary.isSupported()) PlatformLibrary.instance.getMousePosition()
        else Point(-1.0, -1.0)

    fun getPositionInFrame(frame: Frame): Point {
        return if (PlatformLibrary.isSupported()) PlatformLibrary.instance.screenPointToClient(location, frame)
        else Point(-1.0, -1.0)
    }
} }