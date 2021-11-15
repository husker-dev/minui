package com.husker.minui.core.interfaces

import com.husker.minui.geometry.Point

interface Positionable {

    var x: Double
    var y: Double
    var position: Point

    fun onMoved(listener: Runnable)
}