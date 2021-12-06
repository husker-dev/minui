package com.husker
import com.husker.minui.wrapMainThread
import com.husker.minuicore.MColor
import com.husker.minuicore.MCore
import com.husker.minuicore.MCoreProperties
import com.husker.minuicore.pipeline.gl.*
import com.husker.minuicore.platform.win.WinWindowStyle

import com.husker.minuicore.utils.resetTime
import com.husker.minuicore.utils.timeStump
import com.husker.minuicore.window.WindowStyle


fun main(){
    resetTime()
    MCoreProperties.forceLibraryLoad = true
    wrapMainThread {
        //val texture = MCore.pipeline.resourceFactory.createEmptyTexture(100, 100)

        val window = MCore.pipeline.createWindow()
        window.onRender = { gr ->
            val width = window.contentSize.first.toDouble()
            val height = window.contentSize.second.toDouble()

            glMatrixMode(GL_PROJECTION)
            glLoadIdentity()
            glOrtho(0.0, width, height, 0.0, 0.0, 1.0)
            glViewport(0, 0, width.toInt(), height.toInt())

            // Top right
            glBegin(GL_QUADS)
            glColor3d(1.0, 1.0, 1.0)
            glVertex2d(width - 300, 0.0)
            glVertex2d(width - 300, 300.0)
            glVertex2d(width, 300.0)
            glVertex2d(width, 0.0)
            glEnd()

            // Bottom right
            glBegin(GL_QUADS)
            glColor3d(1.0, 1.0, 0.0)
            glVertex2d(width - 300, height - 300.0)
            glVertex2d(width - 300, height)
            glVertex2d(width, height)
            glVertex2d(width, height - 300.0)
            glEnd()

            // Top left
            glBegin(GL_QUADS)
            glColor3d(0.0, 1.0, 0.0)
            glVertex2d(0.0, 0.0)
            glVertex2d(0.0, 300.0)
            glVertex2d(300.0, 300.0)
            glVertex2d(300.0, 0.0)
            glEnd()

            // Bottom left
            glBegin(GL_QUADS)
            glColor3d(0.0, 1.0, 1.0)
            glVertex2d(300.0, height - 300)
            glVertex2d(300.0, height)
            glVertex2d(0.0, height)
            glVertex2d(0.0, height - 300)
            glEnd()

            //gr.drawTexture(texture, 0.0, 0.0, 100.0, 100.0)
        }
        window.apply {
            title = ""
            position = Pair(400, 400)
            size = Pair(500, 500)
            vsync = false
            background = MColor(0.0, 0.0, 1.0, 0.2)
            when(MCore.osName){
                "windows" -> style = WinWindowStyle.micaDark
            }
            minimumSize = Pair(300, 300)
            visible = true
        }

        timeStump()
    }
}




