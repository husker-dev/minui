package com.husker.minuicore.pipeline.gl

import com.husker.minuicore.MColor
import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.MWindow
import com.husker.minuicore.window.WindowStyle
import org.lwjgl.opengl.GL11.*


open class GLWindow: MWindow() {

    final override var windowManager = MCore.platform.createWindowManager()
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
    override val contentSize by windowManager::contentSize

    override var vsync: Boolean
        get() = MCore.invokeOnMainThreadSync { return@invokeOnMainThreadSync nGetVsync(handle) }
        set(value) = MCore.invokeOnMainThreadSync { nSetVsync(handle, value) }

    private var _style: WindowStyle = WindowStyle.default
    final override var style: WindowStyle
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
                nMakeCurrent(handle)
                initGL()

                windowManager.bindHandle(handle)
            }
        }
        windowManager.onClosing = ::fireWindowClosingEvent
        windowManager.onClose = ::fireWindowClosedEvent
        windowManager.onResize = {
            render()
            fireWindowResizedEvent()
        }
        windowManager.onMove = ::fireWindowMovedEvent

        style = WindowStyle.default
        initialized = true
    }

    private fun initGL(){
        glEnable(GL_TEXTURE_2D)
        glEnable(GL_TEXTURE)

        glEnable(GL_ALPHA_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_DST_ALPHA)
    }

    override fun preRender() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(0f, 0f, 0f, 0f)

        val size = contentSize
        glViewport(0, 0, size.first, size.second)
        //glLoadIdentity()
        //glOrtho(0.0, size.first.toDouble(), size.second.toDouble(), 0.0, -1.0, 100.0)
    }

    override fun postRender() {
        glFlush()
        nSwapBuffers(handle)
    }

    override fun setColoredStyle(title: MColor?, text: MColor?, border: MColor?) = windowManager.setColoredStyle(title, text, border)
    override fun setBorderlessStyle() = windowManager.setBorderlessStyle()
    override fun setTitlessStyle() = windowManager.setTitlessStyle()
    override fun setDefaultStyle() = windowManager.setDefaultStyle()

    override fun requestFocus() = windowManager.requestFocus()
    override fun close() = windowManager.close()
    override fun terminate() = windowManager.terminate()
}