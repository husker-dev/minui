package com.husker.minuicore.pipeline

abstract class Graphics {
    abstract fun fillRect(x: Double, y: Double, width: Double, height: Double)
    abstract fun drawTexture(texture: MTexture, x: Double, y: Double, width: Double, height: Double)

    fun reset(){

    }
}