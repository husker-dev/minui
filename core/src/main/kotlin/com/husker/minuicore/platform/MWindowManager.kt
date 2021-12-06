package com.husker.minuicore.platform

import com.husker.minuicore.MColor


abstract class MWindowManager {

    abstract fun bindHandle(handle: Long)

    abstract var position: Pair<Int, Int>
    abstract var size: Pair<Int, Int>
    abstract var title: String
    abstract var visible: Boolean
    abstract var alwaysOnTop: Boolean
    abstract var resizable: Boolean
    abstract var showTaskbarIcon: Boolean
    abstract var minimumSize: Pair<Int, Int>
    abstract var maximumSize: Pair<Int, Int>
    abstract val contentSize: Pair<Int, Int>

    var onClosing: () -> Boolean = { true }
    var onClose: () -> Unit = { }
    var onResize: () -> Unit = { }
    var onMove: () -> Unit = { }

    abstract fun setColoredStyle(title: MColor?, text: MColor?, border: MColor?)
    abstract fun setBorderlessStyle()
    abstract fun setTitlessStyle()
    abstract fun setDefaultStyle()

    abstract fun requestFocus()
    abstract fun close()
    abstract fun terminate()

}