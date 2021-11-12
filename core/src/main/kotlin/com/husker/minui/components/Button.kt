package com.husker.minui.components

import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Graphics

open class Button(text: String = ""): Component() {

    init{
        preferredWidth = 200.0
        preferredHeight = 60.0
    }

    override fun draw(gr: Graphics) {
        gr.paint = Color.Black
        gr.fillRect(0.0, 0.0, width, height)
    }
}