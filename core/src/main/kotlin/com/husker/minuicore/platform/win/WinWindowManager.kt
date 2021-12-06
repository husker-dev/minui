package com.husker.minuicore.platform.win

import com.husker.minuicore.MColor
import com.husker.minuicore.platform.MWindowManager

private external fun nInstall()

class WinWindowManager: MWindowManager() {

    companion object {

        enum class Styles(var id: Int) {
            Default(0),
            Titless(1),
            Borderless(2),
            ColorTitle(3),

            Dark(4),
            Light(5),

            Win11Dark(6),
            Win11Light(7)
        }

        init{
            nInstall()
        }
    }

    var hwnd = 0L

    private external fun nBind(hwnd: Long, callbackObject: Any)
    private external fun nSetWindowPosition(hwnd: Long, x: Int, y: Int)
    private external fun nGetWindowX(hwnd: Long): Int
    private external fun nGetWindowY(hwnd: Long): Int
    private external fun nSetWindowSize(hwnd: Long, width: Int, height: Int)
    private external fun nGetWindowWidth(hwnd: Long): Int
    private external fun nGetWindowHeight(hwnd: Long): Int
    private external fun nSetWindowTitle(hwnd: Long, title: ByteArray)
    private external fun nGetWindowTitle(hwnd: Long): ByteArray
    private external fun nSetWindowVisibility(hwnd: Long, visible: Boolean)
    private external fun nIsWindowVisible(hwnd: Long): Boolean
    private external fun nRequestFocus(hwnd: Long)
    private external fun nTryCloseWindow(hwnd: Long)
    private external fun nDestroyWindow(hwnd: Long)
    private external fun nSetWindowStyleId(hwnd: Long, type: Int)
    private external fun nSetWindowColors(hwnd: Long, title: Int, text: Int, border: Int, defaultTitle: Boolean, defaultText: Boolean, defaultBorder: Boolean)
    private external fun nUpdateExStyle(hwnd: Long, taskbar: Boolean, topMost: Boolean)
    private external fun nSetResizable(hwnd: Long, value: Boolean)
    private external fun nIsResizable(hwnd: Long): Boolean
    private external fun nSetMinimumSize(hwnd: Long, width: Int, height: Int)
    private external fun nSetMaximumSize(hwnd: Long, width: Int, height: Int)
    private external fun nGetClientHeight(hwnd: Long): Int
    private external fun nGetClientWidth(hwnd: Long): Int

    override fun bindHandle(handle: Long) {
        hwnd = handle
        nBind(hwnd, this)
    }

    override var position: Pair<Int, Int>
        get() = Pair(nGetWindowX(hwnd), nGetWindowY(hwnd))
        set(value) = nSetWindowPosition(hwnd, value.first, value.second)

    override var size: Pair<Int, Int>
        get() = Pair(nGetWindowWidth(hwnd), nGetWindowHeight(hwnd))
        set(value) = nSetWindowSize(hwnd, value.first, value.second)

    override var title: String
        get() = nGetWindowTitle(hwnd).utf8Text
        set(value) = nSetWindowTitle(hwnd, value.wideBytes.c_wtype)

    override var visible: Boolean
        get() = nIsWindowVisible(hwnd)
        set(value) = nSetWindowVisibility(hwnd, value)

    private var _alwaysOnTop = false
    override var alwaysOnTop: Boolean
        get() = _alwaysOnTop
        set(value) {
            _alwaysOnTop = value
            updateExStyle()
        }

    override var resizable: Boolean
        get() = nIsResizable(hwnd)
        set(value) = nSetResizable(hwnd, value)

    private var _showTaskbarIcon = true
    override var showTaskbarIcon: Boolean
        get() = _showTaskbarIcon
        set(value) {
            _showTaskbarIcon = value
            updateExStyle()
        }

    private var _minimumSize = Pair(-1, -1)
    override var minimumSize: Pair<Int, Int>
        get() = _minimumSize
        set(value) {
            _minimumSize = value
            nSetMinimumSize(hwnd, value.first, value.second)
        }

    private var _maximumSize = Pair(-1, -1)
    override var maximumSize: Pair<Int, Int>
        get() = _maximumSize
        set(value) {
            _maximumSize = value
            nSetMaximumSize(hwnd, value.first, value.second)
        }

    override val contentSize: Pair<Int, Int>
        get() = Pair(nGetClientWidth(hwnd), nGetClientHeight(hwnd))



    fun setWindowStyle(style: Styles) {
        nSetWindowColors(hwnd, 0, 0, 0, defaultTitle = true, defaultText = true, defaultBorder = true)
        nSetWindowStyleId(hwnd, style.id)
    }
    override fun setDefaultStyle() = setWindowStyle(Styles.Default)
    override fun setTitlessStyle() = setWindowStyle(Styles.Titless)
    override fun setBorderlessStyle() = setWindowStyle(Styles.Borderless)
    override fun setColoredStyle(title: MColor?, text: MColor?, border: MColor?) {
        setWindowStyle(Styles.ColorTitle)
        nSetWindowColors(hwnd,
            title?.toInt(false) ?: 0,
            text?.toInt(false) ?: 0,
            border?.toInt(false) ?: 0,
            title == null, text == null, border == null
            )
    }

    override fun requestFocus() {
        nRequestFocus(hwnd)
        updateExStyle()
    }

    override fun close() = nTryCloseWindow(hwnd)
    override fun terminate() = nDestroyWindow(hwnd)

    @SuppressWarnings("unused") fun onClosingCallback() = onClosing.invoke()
    @SuppressWarnings("unused") fun onCloseCallback() = onClose.invoke()
    @SuppressWarnings("unused") fun onResizeCallback() = onResize.invoke()
    @SuppressWarnings("unused") fun onMoveCallback() = onMove.invoke()

    private fun updateExStyle() = nUpdateExStyle(hwnd, showTaskbarIcon, alwaysOnTop)
}