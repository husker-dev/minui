package com.husker.minui.core

import com.husker.minui.core.exceptions.GLFWContextException
import com.husker.minui.core.listeners.ClosingEvent
import com.husker.minui.layouts.Container
import com.husker.minui.layouts.Pane
import com.husker.minui.core.listeners.KeyActionsReceiver
import com.husker.minui.core.listeners.KeyEvent
import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.geometry.Dimension
import com.husker.minui.geometry.Point
import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Graphics
import com.husker.minui.graphics.Image
import com.husker.minui.natives.PlatformLibrary
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.ARBImaging.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL


open class Frame(): MinUIObject(), KeyActionsReceiver, Drawable, Sizable, Positionable {
    val backend = FrameBackend()

    var background = Color.White

    private var _root: Container = Pane()
    var root: Container
        set(value) {
            _root = value
            _root.width = width
            _root.height = height
            _root.x = 0.0
            _root.y = 0.0
        }
        get() = _root

    private var _title = ""
    var title: String
        set(value) {
            _title = value
            if(backend.initialized) MinUI.invokeLater { glfwSetWindowTitle(backend.window, _title) }
        }
        get() = _title

    private var _width = 400.0
    override var width: Double
        set(value) {
            _width = value
            root.width = value
            if(backend.initialized) MinUI.invokeLater { glfwSetWindowSize(backend.window, width.toInt(), height.toInt()) }
        }
        get() = _width

    private var _height = 400.0
    override var height: Double
        set(value) {
            _height = value
            root.height = value
            if(backend.initialized) MinUI.invokeLater { glfwSetWindowSize(backend.window, width.toInt(), height.toInt()) }
        }
        get() = _height

    override var size: Dimension
        set(value) {
            width = value.width
            height = value.height
        }
        get() = Dimension(width, height)

    private var _x = Double.MIN_VALUE
    override var x: Double
        set(value) {
            _x = value
            if(backend.initialized) MinUI.invokeLater { glfwSetWindowPos(backend.window, x.toInt(), y.toInt()) }
        }
        get() = _x

    private var _y = Double.MIN_VALUE
    override var y: Double
        set(value) {
            _y = value
            if(backend.initialized) MinUI.invokeLater { glfwSetWindowPos(backend.window, x.toInt(), y.toInt()) }
        }
        get() = _y

    override var position: Point
        set(value) {
            x = value.x
            y = value.y
        }
        get() = Point(x, y)

    private var _icon: Image? = null
    var icon: Image?
        set(value) {
            _icon = value
            if(backend.initialized) setWindowIcon(value)
        }
        get() = _icon

    private var _undecorated: Boolean = false
    var undecorated: Boolean
        set(value) {
            _undecorated = value
            if(backend.initialized) MinUI.invokeLaterSync { glfwSetWindowAttrib(backend.window, GLFW_DECORATED, if(value) GLFW_FALSE else GLFW_TRUE) }
        }
        get() = _undecorated

    private var _alwaysOnTop: Boolean = false
    var alwaysOnTop: Boolean
        set(value) {
            _alwaysOnTop = value
            if(backend.initialized) MinUI.invokeLaterSync { glfwSetWindowAttrib(backend.window, GLFW_FLOATING, if(value) GLFW_TRUE else GLFW_FALSE) }
        }
        get() = _alwaysOnTop

    private var _resizable: Boolean = true
    var resizable: Boolean
        set(value) {
            _resizable = value
            if(backend.initialized) MinUI.invokeLaterSync { glfwSetWindowAttrib(backend.window, GLFW_RESIZABLE, if(value) GLFW_TRUE else GLFW_FALSE) }
        }
        get() = _resizable

    private var _visible: Boolean = false
    var visible: Boolean
        set(value) {
            _visible = value
            if(!backend.initialized) init()
            MinUI.invokeLaterSync {
                if(value) glfwShowWindow(backend.window)
                else glfwHideWindow(backend.window)
            }
            _frameVisibleListeners.iterate { it.invoke(value) }
        }
        get() = _visible

    private var _fullscreenDisplay: Display? = null
    var fullscreenDisplay: Display?
        set(value) {
            _fullscreenDisplay = value
            if(backend.initialized) {
                if (value != null) MinUI.invokeLaterSync { glfwSetWindowMonitor(backend.window, value.id, 0, 0, width.toInt(), height.toInt(), value.refreshRate) }
                else MinUI.invokeLaterSync { glfwSetWindowMonitor(backend.window, NULL, 0, 0, width.toInt(), height.toInt(), 60) }
            }
        }
        get() = _fullscreenDisplay

    private var _showTaskbarIcon: Boolean = true
    var showTaskbarIcon: Boolean
        set(value) {
            _showTaskbarIcon = value
            if(backend.initialized) PlatformLibrary.instance.setTaskbarIconEnabled(this@Frame, value)
        }
        get() = _showTaskbarIcon

    private var _vsync: Boolean = true
    var vsync: Boolean
        set(value) {
            _vsync = value
            if(backend.initialized) MinUI.invokeLaterSync {
                MinUI.makeCurrent(backend.window)
                glfwSwapInterval(if(value) GLFW_TRUE else GLFW_FALSE)
            }
        }
        get() = _vsync

    val display: Display
        get() = Display(glfwGetWindowMonitor(backend.window))

    var clipboard: String
        get() = glfwGetClipboardString(backend.window).orEmpty()
        set(value) = glfwSetClipboardString(backend.window, value)

    private var _showed = false
    private val graphics = Graphics()

    private var _keyPressedListeners = ConcurrentArrayList<(event: KeyEvent) -> Unit>()
    private var _keyReleasedListeners = ConcurrentArrayList<(event: KeyEvent) -> Unit>()
    private var _keyTypedListeners = ConcurrentArrayList<(event: KeyEvent) -> Unit>()

    private var _frameVisibleListeners = ConcurrentArrayList<(Boolean) -> Unit>()
    private var _frameClosingListeners = ConcurrentArrayList<(ClosingEvent) -> Unit>()
    private var _frameClosedListeners = ConcurrentArrayList<() -> Unit>()
    private var _frameResizedListeners = ConcurrentArrayList<() -> Unit>()
    private var _frameMovedListeners = ConcurrentArrayList<() -> Unit>()

    private fun init(){
        // 1. Unbind resource's context from its thread
        // 2. Invoke UI-related methods in main thread
        Resources.invokeNoContextSync {
            MinUI.invokeLaterSync {
                glfwDefaultWindowHints()
                glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
                glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE)
                glfwWindowHint(GLFW_RESIZABLE, if(resizable) GLFW_TRUE else GLFW_FALSE)

                backend.window = glfwCreateWindow(_width.toInt(), _height.toInt(), _title, if(fullscreenDisplay != null) fullscreenDisplay!!.id else NULL, Resources.window)
                glfwSetInputMode(backend.window, GLFW_LOCK_KEY_MODS, GLFW_TRUE)

                if(backend.window == NULL)
                    throw RuntimeException("Failed to create the GLFW window")

                prepareWindow()
                backend.initialized = true

                // Update some window properties
                icon = icon
                undecorated = undecorated
                alwaysOnTop = alwaysOnTop
                resizable = resizable
                vsync = vsync
                showTaskbarIcon = showTaskbarIcon

                // Initialize window callbacks
                MinUI.initializeCallbacks(this@Frame)
                MinUI.frames.add(this@Frame)
            }
        }
    }

    constructor(title: String): this(){
        this.title = title
    }

    constructor(title: String, width: Double, height: Double): this(){
        this.title = title
        this._width = width
        this._height = height
    }

    fun close(){
        if(!_showed)
            return
        _showed = false
        glfwSetWindowShouldClose(backend.window, true)
    }

    private fun prepareWindow(){
        val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        val actualX = if(x == Double.MIN_VALUE) (vidMode.width() - width) / 2 else x
        val actualY = if(y == Double.MIN_VALUE) (vidMode.height() - height) / 2 else y

        glfwMakeContextCurrent(backend.window)
        createCapabilities()

        glfwSetWindowPos(backend.window, actualX.toInt(), actualY.toInt())
        glfwSwapInterval(GLFW_FALSE)

        initGLProperties()
        draw()

        glfwMakeContextCurrent(0)
    }

    private fun setWindowIcon(image: Image?){
        if(image == null){
            glfwSetWindowIcon(backend.window, GLFWImage.malloc(0))
            return
        }

        val glfwImage = GLFWImage.malloc()
        val imageBf = GLFWImage.malloc(1)
        glfwImage.set(image.width, image.height, image.data)
        imageBf.put(0, glfwImage)
        glfwSetWindowIcon(backend.window, imageBf)
    }

    private fun initGLProperties(){
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_TEXTURE_2D)

        glEnable(GL_BLEND)
        glBlendEquation(GL_FUNC_ADD)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glEnable(GL_ALPHA_TEST)
        glAlphaFunc(GL_GREATER, 0f)

        glDepthFunc(GL_LEQUAL)

        glMatrixMode(GL_PROJECTION)
    }

    fun requestFocus(){
        MinUI.invokeLaterSync { glfwRestoreWindow(backend.window) }
    }

    fun draw(){
        if(glfwGetCurrentContext() != backend.window)
            throw GLFWContextException("Can't repaint without GLFW context")

        glLoadIdentity()
        glOrtho(0.0, width, height, 0.0, 0.0, 1.0)

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glClearColor(background.red.toFloat(), background.green.toFloat(), background.blue.toFloat(), background.alpha.toFloat())

        graphics.renderingWidth = width
        graphics.renderingHeight = height
        draw(graphics)

        glfwSwapBuffers(backend.window)
    }

    override fun draw(gr: Graphics){
        root.draw(gr)
    }

    override fun addKeyPressListener(listener: (event: KeyEvent) -> Unit) {
        _keyPressedListeners.add(listener)
    }

    override fun addKeyReleasedListener(listener: (event: KeyEvent) -> Unit) {
        _keyReleasedListeners.add(listener)
    }

    override fun addKeyTypedListener(listener: (event: KeyEvent) -> Unit) {
        _keyTypedListeners.add(listener)
    }

    fun addVisibleListener(listener: (Boolean) -> Unit) {
        _frameVisibleListeners.add(listener)
    }

    fun addOnClosingListener(listener: (ClosingEvent) -> Unit) {
        _frameClosingListeners.add(listener)
    }

    fun addOnCloseListener(listener: () -> Unit) {
        _frameClosedListeners.add(listener)
    }

    override fun addOnResizedListener(listener: () -> Unit) {
        _frameResizedListeners.add(listener)
    }

    override fun addOnMovedListener(listener: () -> Unit) {
        _frameMovedListeners.add(listener)
    }

    inner class FrameBackend{
        var window = -1L
        var initialized = false

        fun destroy(){
            MinUI.frames.remove(this@Frame)
            glfwFreeCallbacks(window)
            glfwDestroyWindow(window)
            _frameClosedListeners.iterate { it.invoke() }
        }

        fun onClosing(): Boolean{
            val event = ClosingEvent()
            _frameClosingListeners.iterate { it.invoke(event) }
            return event.close
        }

        fun onResize(width: Int, height: Int){
            _width = width.toDouble()
            _height = height.toDouble()
            root.width = _width
            root.height = _height

            _frameResizedListeners.iterate { it.invoke() }

            glViewport(0, 0, width, height)
            draw()
        }

        fun onMove(x: Int, y: Int){
            _x = x.toDouble()
            _y = y.toDouble()

            _frameMovedListeners.iterate { it.invoke() }
        }

        fun onKeyAction(key: Int, scancode: Int, action: Int, mods: Int){
            if(action == GLFW_RELEASE)
                _keyReleasedListeners.iterate { it.invoke(KeyEvent(key, scancode, KeyEvent.Action.Release, mods)) }
            if(action == GLFW_PRESS)
                _keyPressedListeners.iterate { it.invoke(KeyEvent(key, scancode, KeyEvent.Action.Press, mods)) }
            if(action == GLFW_REPEAT || action == GLFW_PRESS || action == GLFW_KEY_UNKNOWN)
                _keyTypedListeners.iterate { it.invoke(KeyEvent(key, scancode, KeyEvent.Action.Type, mods)) }
        }
    }
}