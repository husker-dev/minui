package com.husker
import com.husker.minui.wrapMainThread
import com.husker.minuicore.MColor
import com.husker.minuicore.MCore
import com.husker.minuicore.utils.resetTime
import com.husker.minuicore.utils.timeStump

fun main(){
    resetTime()
    MCore.forceLibraryLoad = true
    wrapMainThread {
        val window = MCore.pipeline.createWindow()
        window.apply {
            title = "OpenGL Window"
            position = Pair(400, 400)
            size = Pair(500, 500)
            vsync = false
            background = MColor(1.0, 0.0, 0.0, 1.0)
            minimumSize = Pair(300, 300)
            maximumSize = Pair(0, 0)
            visible = true
        }

        timeStump()
    }
}




