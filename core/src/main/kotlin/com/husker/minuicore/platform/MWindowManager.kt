package com.husker.minuicore.platform

abstract class MLWindowManager {

    abstract fun bindHandle(handle: Long)

    abstract var position: Pair<Int, Int>
    abstract var size: Pair<Int, Int>
    abstract var title: String
    abstract var visible: Boolean
    abstract var alwaysOnTop: Boolean
    abstract var undecorated: Boolean
    abstract var resizable: Boolean
    abstract var showTaskbarIcon: Boolean

    var onClosing: () -> Boolean = { true }
    var onClose: () -> Unit = { }
    var onResize: () -> Unit = { }
    var onMove: () -> Unit = { }

    abstract fun requestFocus()
    abstract fun close()
    abstract fun terminate()

}