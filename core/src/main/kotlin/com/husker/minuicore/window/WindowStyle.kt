package com.husker.minuicore.window

import com.husker.minuicore.MColor
import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.MWindow

abstract class WindowStyle {
    companion object {
        val default = Default()
        val titless = Titless()
        val borderless = Borderless()
    }

    abstract fun apply(window: MWindow)
    abstract fun clear(window: MWindow)

    // Handled in MWindow
    open class Default: WindowStyle(){
        override fun apply(window: MWindow) = window.setDefaultStyle()
        override fun clear(window: MWindow) {}
    }

    open class Titless: WindowStyle(){
        override fun apply(window: MWindow) = window.setTitlessStyle()
        override fun clear(window: MWindow) {}
    }

    open class Borderless: WindowStyle(){
        override fun apply(window: MWindow) = window.setBorderlessStyle()
        override fun clear(window: MWindow) {}
    }

    open class Colored(var title: MColor?, var text: MColor?, var border: MColor?): WindowStyle(){
        override fun apply(window: MWindow) = window.setColoredStyle(title, text, border)
        override fun clear(window: MWindow) {}
    }

    abstract class ThemedStyle: WindowStyle(){
        private val listeners = hashMapOf<MWindow, (Boolean) -> Unit>()

        override fun apply(window: MWindow) {
            listeners[window] = { isDark: Boolean -> applyTheme(window, isDark) }
            MCore.platform.onThemeChanged(listeners[window]!!)
            applyTheme(window, MCore.platform.isDarkTheme)
        }

        override fun clear(window: MWindow) {
            MCore.platform.removeThemeChangedListener(listeners[window]!!)
            listeners.remove(window)
        }

        protected abstract fun applyTheme(window: MWindow, isDark: Boolean)
    }
}