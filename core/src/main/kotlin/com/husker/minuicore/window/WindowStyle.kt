package com.husker.minuicore.window

import com.husker.minuicore.MColor
import com.husker.minuicore.pipeline.MWindow

abstract class WindowStyle {
    companion object {
        val default = Default()
        val titless = Titless()
        val borderless = Borderless()
    }

    abstract fun apply(window: MWindow)

    // Handled in MWindow
    open class Default: WindowStyle(){
        override fun apply(window: MWindow) = window.setDefaultStyle()
    }

    open class Titless: WindowStyle(){
        override fun apply(window: MWindow) = window.setTitlessStyle()
    }

    open class Borderless: WindowStyle(){
        override fun apply(window: MWindow) = window.setBorderlessStyle()
    }

    open class Colored(var title: MColor?, var text: MColor?, var border: MColor?): WindowStyle(){
        override fun apply(window: MWindow) = window.setColoredStyle(title, text, border)
    }

}