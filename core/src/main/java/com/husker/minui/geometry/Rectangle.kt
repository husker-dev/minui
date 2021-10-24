package com.husker.minui.geometry

data class Rectangle(
    var x: Double,
    var y: Double,
    var width: Double,
    var height: Double
): Shape() {

    constructor(width: Double, height: Double): this(0.0, 0.0, width, height)

    override val bounds: Rectangle
        get() = Rectangle(x, y, width, height)

    override fun contains(x: Double, y: Double): Boolean {
        return x >= this.x && y >= this.y && x <= this.width + this.x && y <= this.height + this.y
    }
}