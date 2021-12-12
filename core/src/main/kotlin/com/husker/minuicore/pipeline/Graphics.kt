package com.husker.minuicore.pipeline

import com.husker.minuicore.MColor
import com.husker.minuicore.transform.Matrix

abstract class Graphics {

    open lateinit var transform: Matrix

    abstract var paint: Paint
    abstract var color: MColor

    abstract fun fillRect(x: Double, y: Double, width: Double, height: Double)
    abstract fun drawTexture(texture: MTexture, x: Double, y: Double, width: Double, height: Double)

    open fun reset(width: Int, height: Int){
        transform = Matrix.identity()
    }

    abstract class Paint{
        abstract fun apply()
    }
}