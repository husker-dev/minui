package com.husker.minuicore

import kotlin.math.roundToInt

class MColor(val red: Double, val green: Double, val blue: Double, val alpha: Double) {

    fun toInt(useAlpha: Boolean = true): Int{
        val r = (255 * red).roundToInt() shl 16
        val g = (255 * green).roundToInt() shl 8
        val b = (255 * blue).roundToInt()
        return if(useAlpha){
            val a = (255 * alpha).roundToInt() shl 24
            a or r or g or b
        }else
            r or g or b
    }
}