package com.husker.minuicore.window

import com.husker.minuicore.pipeline.MWindow

abstract class MWindowStyle {
    companion object {
        val default = DefaultStyle()
        val titless = TitlessStyle()
        val borderless = BorderlessStyle()
    }

    abstract fun apply(window: MWindow)

    // Handled in MWindow
    open class DefaultStyle: MWindowStyle(){
        override fun apply(window: MWindow) = window.setDefaultStyle()
    }

    open class TitlessStyle: MWindowStyle(){
        override fun apply(window: MWindow) = window.setTitlessStyle()
    }

    open class BorderlessStyle: MWindowStyle(){
        override fun apply(window: MWindow) = window.setBorderlessStyle()
    }

}