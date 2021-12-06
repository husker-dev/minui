package com.husker.minuicore.platform.win

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

    class AutoTheme: WindowStyle(){
        override fun apply(window: MWindow) {
            //(window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.)
        }
    }

    class Light: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Light)
        }
    }

    class Dark: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Dark)
        }
    }

    class Win11: WindowStyle(){
        override fun apply(window: MWindow) {
            //(window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Win11AutoTheme)
        }
    }

    class Win11Light: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Win11Light)
        }
    }

    class Win11Dark: WindowStyle(){
        override fun apply(window: MWindow) {
            (window.windowManager as WinWindowManager).setWindowStyle(WinWindowManager.Companion.Styles.Win11Dark)
        }
    }
}