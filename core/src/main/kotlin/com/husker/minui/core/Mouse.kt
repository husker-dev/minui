package com.husker.minui.core

import com.husker.minui.geometry.Point
import com.husker.minui.natives.PlatformLibrary

object Mouse {

    val location: Point
        get() = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.getMousePosition()
                else Point(-1.0, -1.0)

    fun getPositionInFrame(frame: Frame): Point{
        return if(PlatformLibrary.isSupported()) PlatformLibrary.instance.screenPointToClient(location, frame)
        else Point(-1.0, -1.0)
    }
}