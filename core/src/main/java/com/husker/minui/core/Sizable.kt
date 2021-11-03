package com.husker.minui.core

import com.husker.minui.geometry.Dimension

interface Sizable {

    var width: Double
    var height: Double
    var size: Dimension

    fun onResize(listener: Runnable)
}