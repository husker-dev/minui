package com.husker.minuicore.pipeline.gl

import com.husker.minuicore.MColor
import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.MWindow
import com.husker.minuicore.window.MWindowStyle

class GLWindow: MWindow() {

    var windowManager = MCore.platform.createWindowManager()
    private val resourceFactory = MCore.pipeline.resourceFactory as GLResourceFactory
    var initialized = false
    var handle = 0L

    override var background = MColor(1.0, 1.0, 1.0, 1.0)
    override var visible by windowManager::visible
    override var title by windowManager::title
    override var size by windowManager::size
    override var position by windowManager::position
    override var alwaysOnTop by windowManager::alwaysOnTop
    override var resizable by windowManager::resizable
    override var showTaskbarIcon by windowManager::showTaskbarIcon
    override var minimumSize by windowManager::minimumSize
    override var maximumSize by windowManager::maximumSize

    override var vsync: Boolean
        get() = MCore.invokeOnMainThreadSync { return@invokeOnMainThreadSync nGetVsync(handle) }
        set(value) = MCore.invokeOnMainThreadSync { nSetVsync(handle, value) }

    private var _style: MWindowStyle = MWindowStyle.default
    override var style: MWindowStyle
        get() = _style
        set(value) {
            _style = value
            _style.apply(this)
        }

    init{
        resourceFactory.invokeSync {
            it.clearContext()
            MCore.invokeOnMainThreadSync {
                handle = nCreateWindow(resourceFactory.handle)
                windowManager.bindHandle(handle)
            }
        }
        windowManager.onClosing = this::fireWindowClosingEvent
        windowManager.onClose = this::fireWindowClosedEvent
        windowManager.onResize = {
            render()
            fireWindowResizedEvent()
        }
        windowManager.onMove = this::fireWindowMovedEvent

        style = MWindowStyle.default
        initialized = true
    }

    fun render() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(background.red.toFloat(), background.green.toFloat(), background.blue.toFloat(), background.alpha.toFloat())

        nSwapBuffers(handle)
    }

    override fun setBorderlessStyle() = windowManager.setBorderlessStyle()
    override fun setTitlessStyle() = windowManager.setTitlessStyle()
    override fun setDefaultStyle() = windowManager.setDefaultStyle()

    override fun requestFocus() = windowManager.requestFocus()
    override fun close() = windowManager.close()
    override fun terminate() = windowManager.terminate()
}