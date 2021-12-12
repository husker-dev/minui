package com.husker.minuicore.platform.win

import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.MWindow
import com.husker.minuicore.window.WindowStyle

class WinWindowStyle {

    companion object {
        val default = WindowStyle.Default()
        val titless = WindowStyle.Titless()
        val borderless = WindowStyle.Borderless()

        val autoTheme = AutoTheme()
        val light = Light()
        val dark = Dark()
        val mica = Win11()
        val micaDark = Win11Dark()
        val micaLight = Win11Light()
    }

    class AutoTheme: WindowStyle.ThemedStyle(){
        override fun applyTheme(window: MWindow, isDark: Boolean){
            val themeId = if (isDark)
                WinWindowManager.Companion.Styles.Dark
            else WinWindowManager.Companion.Styles.Light
            (window.windowManager as WinWindowManager).setWindowStyle(themeId)
        }
    }

    class Light: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Light)
        }
        override fun clear(window: MWindow) {}
    }

    class Dark: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Dark)
        }
        override fun clear(window: MWindow) {}
    }

    class Win11: WindowStyle.ThemedStyle(){
        override fun applyTheme(window: MWindow, isDark: Boolean){
            val themeId = if (isDark)
                WinWindowManager.Companion.Styles.Win11Dark
            else WinWindowManager.Companion.Styles.Win11Light
            (window.windowManager as WinWindowManager).setWindowStyle(themeId)
        }
    }

    class Win11Light: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Win11Light)
        }
        override fun clear(window: MWindow) {}
    }

    class Win11Dark: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Win11Dark)
        }
        override fun clear(window: MWindow) {}
    }
}