package com.husker.minui.core

import com.husker.minui.components.*
import com.husker.minui.core.input.Mouse
import com.husker.minui.core.input.MouseAction
import com.husker.minui.core.interfaces.Drawable
import com.husker.minui.core.interfaces.Positionable
import com.husker.minui.core.interfaces.Sizable
import com.husker.minui.geometry.*
import com.husker.minui.graphics.*
import com.husker.minui.layouts.*
import com.husker.minui.core.listeners.*
import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.natives.PlatformLibrary
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.ARBImaging.GL_FUNC_ADD
import org.lwjgl.opengl.ARBImaging.glBlendEquation
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import java.util.function.Consumer
import kotlin.system.exitProcess


open class Frame(
    title: String = "",
    icon: Image? = null
): MinUIObject(), KeyEventsReceiver, MouseEventsReceiver, Drawable, Sizable, Positionable {
    val backend = FrameBackend()

    enum class FrameState {
        Default,
        Minimized,
        Maximized,
        Fullscreen,
        WindowedFullscreen
    }

    enum class CloseOperation{
        ExitProgram,
        Nothing
    }

    var background = Color.White
    var closeOperation = CloseOperation.Nothing

    private var _state = FrameState.Default
    var state: FrameState
        get() = _state
        set(value) {
            if (backend.initialized) {
                MinUI.invokeLaterSync {
                    when (value) {
                        FrameState.Default -> {
                            if (_state == FrameState.Fullscreen || _state == FrameState.WindowedFullscreen)
                                glfwSetWindowMonitor(backend.window, NULL, y.toInt(), x.toInt(), width.toInt(), height.toInt(), 0)
                            glfwRestoreWindow(backend.window)
                        }
                        FrameState.Minimized -> {
                            if (_state == FrameState.Fullscreen || _state == FrameState.WindowedFullscreen)
                                glfwSetWindowMonitor(backend.window, NULL, y.toInt(), x.toInt(), width.toInt(), height.toInt(), 0)
                            glfwIconifyWindow(backend.window)
                        }
                        FrameState.Maximized -> {
                            if (_state == FrameState.Fullscreen || _state == FrameState.WindowedFullscreen)
                                glfwSetWindowMonitor(backend.window, NULL, y.toInt(), x.toInt(), width.toInt(), height.toInt(), 0)
                            glfwMaximizeWindow(backend.window)
                        }
                        FrameState.Fullscreen -> glfwSetWindowMonitor(backend.window, fullscreenDisplay.id, 0, 0, width.toInt(), height.toInt(), fullscreenDisplay.refreshRate)
                        FrameState.WindowedFullscreen -> glfwSetWindowMonitor(backend.window, fullscreenDisplay.id, 0, 0, fullscreenDisplay.width, fullscreenDisplay.height, fullscreenDisplay.refreshRate)
                    }
                }
            }
            _state = value
        }

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

    private val layerPane = LayerPane()

    private var _title = title
    var title: String
        set(value) {
            _title = value
            if (backend.initialized) MinUI.invokeLater { glfwSetWindowTitle(backend.window, _title) }
        }
        get() = _title

    private var _width = 400.0
    override var width: Double
        set(value) {
            _width = value
            root.width = value
            if (backend.initialized) MinUI.invokeLater { glfwSetWindowSize(backend.window, width.toInt(), height.toInt()) }
        }
        get() = _width

    private var _height = 400.0
    override var height: Double
        set(value) {
            _height = value
            root.height = value
            if (backend.initialized) MinUI.invokeLater { glfwSetWindowSize(backend.window, width.toInt(), height.toInt()) }
        }
        get() = _height

    override var size: Dimension
        set(value) {
            width = value.width
            height = value.height
        }
        get() = Dimension(width, height)

    private var _minimumSize = Dimension(-1.0, -1.0)
    var minimumSize: Dimension
        set(value) {
            _minimumSize = value
            if (backend.initialized) MinUI.invokeLater {
                glfwSetWindowSizeLimits(backend.window,
                    (_minimumSize.width * display.dpi).toInt(), (_minimumSize.height * display.dpi).toInt(),
                    (_maximumSize.width * display.dpi).toInt(), (_maximumSize.height * display.dpi).toInt()
                )
            }
        }
        get() = _minimumSize

    private var _maximumSize = Dimension(-1.0, -1.0)
    var maximumSize: Dimension
        set(value) {
            _maximumSize = value
            if (backend.initialized) MinUI.invokeLater {
                glfwSetWindowSizeLimits(backend.window,
                    (_minimumSize.width * display.dpi).toInt(), (_minimumSize.height * display.dpi).toInt(),
                    (_maximumSize.width * display.dpi).toInt(), (_maximumSize.height * display.dpi).toInt()
                )
            }
        }
        get() = _maximumSize

    private var _x = Double.MIN_VALUE
    override var x: Double
        set(value) {
            _x = value
            if (backend.initialized) MinUI.invokeLater { glfwSetWindowPos(backend.window, x.toInt(), y.toInt()) }
        }
        get() = _x

    private var _y = Double.MIN_VALUE
    override var y: Double
        set(value) {
            _y = value
            if (backend.initialized) MinUI.invokeLater { glfwSetWindowPos(backend.window, x.toInt(), y.toInt()) }
        }
        get() = _y

    override var position: Point
        set(value) {
            x = value.x
            y = value.y
        }
        get() = Point(x, y)

    var bounds: Rectangle
        set(value) {
            position = Point(value.x, value.y)
            size = Dimension(value.width, value.height)
        }
        get() = Rectangle(x, y, width, height)

    private var _icon: Image? = icon
    var icon: Image?
        set(value) {
            _icon = value
            if (backend.initialized) setWindowIcon(value)
        }
        get() = _icon

    private var _undecorated: Boolean = false
    var undecorated: Boolean
        set(value) {
            _undecorated = value
            if (backend.initialized) MinUI.invokeLaterSync {
                glfwSetWindowAttrib(backend.window, GLFW_DECORATED, if (value) GLFW_FALSE else GLFW_TRUE)
            }
        }
        get() = _undecorated

    private var _alwaysOnTop: Boolean = false
    var alwaysOnTop: Boolean
        set(value) {
            _alwaysOnTop = value
            if (backend.initialized) MinUI.invokeLaterSync {
                glfwSetWindowAttrib(
                    backend.window,
                    GLFW_FLOATING,
                    if (value) GLFW_TRUE else GLFW_FALSE
                )
            }
        }
        get() = _alwaysOnTop

    private var _resizable: Boolean = true
    var resizable: Boolean
        set(value) {
            _resizable = value
            if (backend.initialized) MinUI.invokeLaterSync {
                glfwSetWindowAttrib(
                    backend.window,
                    GLFW_RESIZABLE,
                    if (value) GLFW_TRUE else GLFW_FALSE
                )
            }
        }
        get() = _resizable

    private var _visible: Boolean = false
    var visible: Boolean
        set(value) {
            _visible = value
            if (!backend.initialized) init()
            MinUI.invokeLaterSync {
                if (value) glfwShowWindow(backend.window)
                else glfwHideWindow(backend.window)
            }
            _frameVisibleListeners.iterate { it.accept(value) }
        }
        get() = _visible

    private var _fullscreenDisplay: Display? = null
    var fullscreenDisplay: Display
        set(value) {
            _fullscreenDisplay = value
        }
        get() = if (_fullscreenDisplay == null) Display.default else _fullscreenDisplay!!

    private var _showTaskbarIcon: Boolean = true
    var showTaskbarIcon: Boolean
        set(value) {
            _showTaskbarIcon = value
            if (backend.initialized) PlatformLibrary.instance.setTaskbarIconEnabled(this@Frame, value)
        }
        get() = _showTaskbarIcon

    private var _vsync: Boolean = true
    var vsync: Boolean
        set(value) {
            _vsync = value
            if (backend.initialized) MinUI.invokeLaterSync {
                glfwSwapInterval(if (value) GLFW_TRUE else GLFW_FALSE)
            }
        }
        get() = _vsync

    val display: Display
        get() = if (backend.initializeEnded) Display.ofFrame(this) else Display.default

    val mousePosition: Point
        get() = Mouse.getPositionInFrame(this)

    private val graphics = Graphics()

    private var _keyPressedListeners = ConcurrentArrayList<Consumer<KeyEvent>>()
    private var _keyReleasedListeners = ConcurrentArrayList<Consumer<KeyEvent>>()
    private var _keyTypedListeners = ConcurrentArrayList<Consumer<KeyEvent>>()

    private var _frameVisibleListeners = ConcurrentArrayList<Consumer<Boolean>>()
    private var _frameClosingListeners = ConcurrentArrayList<Consumer<ClosingEvent>>()
    private var _frameClosedListeners = ConcurrentArrayList<Runnable>()
    private var _frameResizedListeners = ConcurrentArrayList<Runnable>()
    private var _frameMovedListeners = ConcurrentArrayList<Runnable>()

    private var _mouseMovedListeners = ConcurrentArrayList<Runnable>()
    private var _mousePressedListeners = ConcurrentArrayList<Consumer<MouseEvent>>()
    private var _mouseReleasedListeners = ConcurrentArrayList<Consumer<MouseEvent>>()
    private var _mouseClickedListeners = ConcurrentArrayList<Consumer<MouseEvent>>()

    private var _frameMinimizedListeners = ConcurrentArrayList<Runnable>()
    private var _frameMaximizedListeners = ConcurrentArrayList<Runnable>()
    private var _frameRestoredListeners = ConcurrentArrayList<Runnable>()

    private fun init(){
        // 1. Unbind resource's context from its thread
        // 2. Invoke UI-related methods in main thread
        Resources.invokeNoContextSync {
            MinUI.invokeLaterSync {
                glfwDefaultWindowHints()
                glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
                glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE)
                glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE)
                glfwWindowHint(GLFW_WIN32_KEYBOARD_MENU, GLFW_TRUE)

                backend.window = glfwCreateWindow(width.toInt(), height.toInt(), _title, NULL, Resources.window)
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
                showTaskbarIcon = showTaskbarIcon
                minimumSize = minimumSize   // maximumSize is the same

                // Initialize window callbacks
                MinUI.initializeCallbacks(this@Frame)
                MinUI.frames.add(this@Frame)

                // Show window
                state = state

                // Update properties after creation context
                vsync = vsync

                backend.initializeEnded = true
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

    fun show(){
        visible = true
    }

    fun close(){
        if(!_visible)
            return
        glfwSetWindowShouldClose(backend.window, true)
    }

    private fun prepareWindow(){
        val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        val actualX = if(x == Double.MIN_VALUE) (vidMode.width() - width * display.dpi) / 2 else x
        val actualY = if(y == Double.MIN_VALUE) (vidMode.height() - height * display.dpi) / 2 else y

        glfwMakeContextCurrent(backend.window)
        createCapabilities()

        glfwSetWindowPos(backend.window, actualX.toInt(), actualY.toInt())
        glfwSwapInterval(GLFW_FALSE)

        backend.initGLProperties()
        backend.drawGL()

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

    private fun splash(splashImage: Image, initBlock: Runnable){
        splash(ImageView(splashImage), initBlock)
    }

    private fun splash(splashComponent: Component, initBlock: Runnable){

    }

    fun requestFocus(){
        MinUI.invokeLaterSync { glfwRestoreWindow(backend.window) }
    }

    override fun draw(gr: Graphics){
        root.draw(gr)
    }

    override fun onKeyPress(listener: Consumer<KeyEvent>) {
        _keyPressedListeners.add(listener)
    }

    override fun onKeyRelease(listener: Consumer<KeyEvent>) {
        _keyReleasedListeners.add(listener)
    }

    override fun onKeyType(listener: Consumer<KeyEvent>) {
        _keyTypedListeners.add(listener)
    }

    fun onVisibleChanges(listener: Consumer<Boolean>) {
        _frameVisibleListeners.add(listener)
    }

    fun onClosing(listener: Consumer<ClosingEvent>) {
        _frameClosingListeners.add(listener)
    }

    fun onClose(listener: Runnable) {
        _frameClosedListeners.add(listener)
    }

    override fun onResize(listener: Runnable) {
        _frameResizedListeners.add(listener)
    }

    override fun onMoved(listener: Runnable) {
        _frameMovedListeners.add(listener)
    }

    override fun onMousePress(listener: Consumer<MouseEvent>) {
        _mousePressedListeners.add(listener)
    }

    override fun onMouseRelease(listener: Consumer<MouseEvent>) {
        _mouseReleasedListeners.add(listener)
    }

    override fun onMouseClick(listener: Consumer<MouseEvent>) {
        _mouseClickedListeners.add(listener)
    }

    fun onMaximize(listener: Runnable) {
        _frameMaximizedListeners.add(listener)
    }

    fun onMinimize(listener: Runnable) {
        _frameMinimizedListeners.add(listener)
    }

    fun onRestore(listener: Runnable) {
        _frameRestoredListeners.add(listener)
    }

    inner class FrameBackend{
        var window = -1L
        var initialized = false
        var initializeEnded = false

        var lastMousePressedEvent: MouseEvent? = null
        var lastMouseReleaseEvent: MouseEvent? = null
        var lastMouseClickedEvent: MouseEvent? = null

        fun destroy(){
            MinUI.frames.remove(this@Frame)
            glfwFreeCallbacks(window)
            glfwDestroyWindow(window)
            _frameClosedListeners.iterate { it.run() }
            if(closeOperation == CloseOperation.ExitProgram) exitProcess(0)
        }

        fun updateDPI(){
            /*
            if(lastDisplayId != display.id){
                lastDisplayId = display.id
                dpi = display.contentScale.width
            }

             */
        }

        fun initGLProperties(){
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

        fun drawGL(){
            glLoadIdentity()
            glOrtho(0.0, width, height, 0.0, 0.0, 1.0)

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glClearColor(background.red.toFloat(), background.green.toFloat(), background.blue.toFloat(), background.alpha.toFloat())

            graphics.renderingWidth = width
            graphics.renderingHeight = height
            draw(graphics)

            glfwSwapBuffers(backend.window)
        }

        fun onClosing(): Boolean{
            val event = ClosingEvent()
            _frameClosingListeners.iterate { it.accept(event) }
            return event.close
        }

        fun onResize(width: Int, height: Int){
            _width = width.toDouble()
            _height = height.toDouble()
            root.width = _width
            root.height = _height

            _frameResizedListeners.iterate { it.run() }
            updateDPI()

            glViewport(0, 0, width, height)
            drawGL()
        }

        fun onMove(x: Int, y: Int){
            // If window is not in main display, then set 'dpi' to main's display
            val delta = if(Point(x.toDouble(), y.toDouble()) in Display.default.bounds) 1.0 else Display.default.dpi

            _x = x.toDouble() / delta
            _y = y.toDouble() / delta
            _frameMovedListeners.iterate { it.run() }
            updateDPI()
        }

        fun onMaximize(maximized: Boolean){
            if(maximized) {
                _state = FrameState.Maximized
                _frameMaximizedListeners.iterate { it.run() }
            }else {
                _state = FrameState.Default
                _frameRestoredListeners.iterate { it.run() }
            }
        }

        fun onIconify(iconified: Boolean){
            if(iconified) {
                _state = FrameState.Minimized
                _frameMinimizedListeners.iterate { it.run() }
            }else {
                _state = FrameState.Default
                _frameRestoredListeners.iterate { it.run() }
            }
        }

        fun onKeyAction(key: Int, scancode: Int, action: Int, mods: Int){
            if(action == GLFW_RELEASE)
                _keyReleasedListeners.iterate { it.accept(KeyEvent(key, scancode, KeyEvent.Action.Release, mods)) }
            if(action == GLFW_PRESS)
                _keyPressedListeners.iterate { it.accept(KeyEvent(key, scancode, KeyEvent.Action.Press, mods)) }
            if(action == GLFW_REPEAT || action == GLFW_PRESS || action == GLFW_KEY_UNKNOWN)
                _keyTypedListeners.iterate { it.accept(KeyEvent(key, scancode, KeyEvent.Action.Type, mods)) }
        }

        fun onMouseMove() {
            _mouseMovedListeners.iterate { it.run() }
        }

        fun onMouseAction(button: Int, action: Int, mods: Int){
            if(action == GLFW_PRESS) {
                lastMousePressedEvent = MouseEvent(button, MouseAction.Press, mods, 1, mousePosition)
                _mousePressedListeners.iterate { it.accept(lastMousePressedEvent!!) }
            }
            if(action == GLFW_RELEASE){
                if(lastMousePressedEvent != null &&
                    lastMousePressedEvent!!.position == mousePosition
                ){
                    if(lastMouseClickedEvent != null &&
                        lastMouseClickedEvent!!.position == mousePosition &&
                        System.currentTimeMillis() - lastMouseClickedEvent!!.time < 400
                    ){
                        // Not first click
                        lastMouseClickedEvent = MouseEvent(button, MouseAction.Click, mods, lastMouseClickedEvent!!.clickCount + 1, mousePosition)
                        _mouseClickedListeners.iterate { it.accept(lastMouseClickedEvent!!) }
                    }else {
                        // First click
                        lastMouseClickedEvent = MouseEvent(button, MouseAction.Click, mods, 1, mousePosition)
                        _mouseClickedListeners.iterate { it.accept(lastMouseClickedEvent!!) }
                    }
                }
                // Release
                lastMouseReleaseEvent = MouseEvent(button, MouseAction.Release, mods, 1, mousePosition)
                _mouseReleasedListeners.iterate { it.accept(lastMouseReleaseEvent!!) }
            }
        }
    }
}