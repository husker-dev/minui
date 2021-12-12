package com.husker
import com.husker.minui.wrapMainThread
import com.husker.minuicore.MColor
import com.husker.minuicore.MCore
import com.husker.minuicore.MCoreProperties
import com.husker.minuicore.pipeline.MTexture
import com.husker.minuicore.platform.win.WinWindowStyle

import com.husker.minuicore.utils.resetTime
import com.husker.minuicore.utils.timeStump
import kotlin.concurrent.thread


fun main(){
    resetTime()
    MCoreProperties.forceLibraryLoad = true
    wrapMainThread {
        var texture: MTexture? = null

        val window = MCore.pipeline.createWindow()
        window.onRender = { gr ->
            if(texture != null)
                gr.drawTexture(texture!!, 0.0, 0.0, window.contentSize.first.toDouble(), window.contentSize.second.toDouble())
            gr.color = MColor(0.0, 0.0, 1.0, 1.0)
            gr.fillRect(0.0, 0.0, 300.0, 300.0)
        }
        window.apply {
            title = ""
            position = Pair(400, 400)
            size = Pair(500, 500)
            vsync = false
            background = MColor(0.0, 0.0, 0.0, 0.0)
            when(MCore.osName){
                "windows" -> style = WinWindowStyle.mica
            }
            minimumSize = Pair(300, 300)
            visible = true
        }

        timeStump()

        thread {
            texture = MCore.pipeline.resourceFactory.createTextureFromURL("https://sun9-41.userapi.com/impg/XoyY5me3SmvQ8jRdYuheId8NR1gzhQ3h2HxH_Q/zwBGEbqR6v4.jpg?size=578x585&quality=96&sign=957dcf9f48af35d493f8f49eb929c3ec&type=album")
        }
    }


}




