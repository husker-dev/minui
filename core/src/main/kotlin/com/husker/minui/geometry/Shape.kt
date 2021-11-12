package com.husker.minui.geometry

abstract class Shape {
    abstract val bounds: Rectangle

    abstract fun contains(x: Double, y: Double): Boolean
    operator fun contains(point: Point): Boolean = contains(point.x, point.y)
}

