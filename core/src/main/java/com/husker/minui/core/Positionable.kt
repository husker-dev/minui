package com.husker.minui.core

import com.husker.minui.geometry.Point

interface Positionable {

    var x: Double
    var y: Double
    var position: Point

    fun addOnMovedListener(listener: () -> Unit)
}