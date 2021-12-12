package com.husker.minuicore.pipeline

import com.husker.minuicore.MColor
import com.husker.minuicore.MCore
import com.husker.minuicore.pipeline.gl.GLGraphics
import com.husker.minuicore.platform.MWindowManager
import com.husker.minuicore.utils.concurrentArrayList
import com.husker.minuicore.window.WindowStyle

abstract class MWindow {

    abstract val windowManager: MWindowManager

    abstract var visible: Boolean
    abstract var title: String
    abstract var size: Pair<Int, Int>
    abstract var position: Pair<Int, Int>
    abstract var minimumSize: Pair<Int, Int>
    abstract var maximumSize: Pair<Int, Int>
    abstract var alwaysOnTop: Boolean
    abstract var resizable: Boolean
    abstract var showTaskbarIcon: Boolean
    abstract var vsync : Boolean
    abstract var background: MColor
    abstract var style: WindowStyle
    abstract val contentSize: Pair<Int, Int>

    val graphics = MCore.pipeline.createGraphics() as GLGraphics
    var onRender: (Graphics) -> Unit = {}

    init{
        MCore.windows.add(this)
    }

    fun render(){
        preRender()
        val size = contentSize
        graphics.reset(size.first, size.second)

        graphics.color = background
        graphics.fillRect(0.0, 0.0, size.first.toDouble(), size.second.toDouble())

        onRender.invoke(graphics)
        postRender()
    }

    abstract fun preRender()
    abstract fun postRender()

    abstract fun setColoredStyle(title: MColor?, text: MColor?, border: MColor?)
    abstract fun setBorderlessStyle()
    abstract fun setTitlessStyle()
    abstract fun setDefaultStyle()

    abstract fun requestFocus()

    abstract fun close()
    abstract fun terminate()

    /** =================
     *      Listeners
     *  =================
     */
    // Key listeners -> key, shift, ctrl, alt, num
    var keyPressedListeners = concurrentArrayList<(Int, Boolean, Boolean, Boolean, Boolean) -> Unit>()
    var keyReleasedListeners = concurrentArrayList<(Int, Boolean, Boolean, Boolean, Boolean) -> Unit>()
    var keyTypedListeners = concurrentArrayList<(Int, Boolean, Boolean, Boolean, Boolean) -> Unit>()

    // Size, position listeners
    var windowResizedListeners = concurrentArrayList<() -> Unit>()
    var windowMovedListeners = concurrentArrayList<() -> Unit>()

    // State listeners
    var windowClosingListeners = concurrentArrayList<() -> Boolean>()
    var windowClosedListeners = concurrentArrayList<() -> Unit>()
    var windowVisibleListeners = concurrentArrayList<(Boolean) -> Unit>()

    // Mouse listeners -> button, x, y, shift, ctrl, alt, num
    var mousePressedListeners = concurrentArrayList<(Int, Int, Int, Boolean, Boolean, Boolean, Boolean) -> Unit>()
    var mouseReleasedListeners = concurrentArrayList<(Int, Int, Int, Boolean, Boolean, Boolean, Boolean) -> Unit>()
    var mouseClickedListeners = concurrentArrayList<(Int, Int, Int, Boolean, Boolean, Boolean, Boolean) -> Unit>()
    var mouseMovedListeners = concurrentArrayList<() -> Unit>()

    /** ==============
     *      Events
     *  ==============
     */
    fun fireKeyPressedEvent(key: Int, shift: Boolean, ctrl: Boolean, alt: Boolean, numlock: Boolean) =
        keyPressedListeners.iterate { it(key, shift, ctrl, alt, numlock) }

    fun fireKeyReleasedEvent(key: Int, shift: Boolean, ctrl: Boolean, alt: Boolean, numlock: Boolean) =
        keyReleasedListeners.iterate { it(key, shift, ctrl, alt, numlock) }

    fun fireKeyTypedEvent(key: Int, shift: Boolean, ctrl: Boolean, alt: Boolean, numlock: Boolean) =
        keyTypedListeners.iterate { it(key, shift, ctrl, alt, numlock) }

    fun fireWindowResizedEvent() =
        windowResizedListeners.iterate { it() }

    fun fireWindowMovedEvent() =
        windowMovedListeners.iterate { it() }

    fun fireWindowClosingEvent(): Boolean {
        var result = true
        windowClosingListeners.iterate { if(!it.invoke()) result = false }
        return result
    }

    fun fireWindowClosedEvent() {
        MCore.windows.remove(this)
        windowClosedListeners.iterate { it() }
    }

    fun fireWindowVisibleEvent(visible: Boolean) =
        windowVisibleListeners.iterate { it(visible) }

    fun fireMouseMovedEvent() =
        mouseMovedListeners.iterate { it() }

    fun fireMousePressedEvent(button: Int, x: Int, y: Int, shift: Boolean, ctrl: Boolean, alt: Boolean, numlock: Boolean) =
        mousePressedListeners.iterate { it(button, x, y, shift, ctrl, alt, numlock) }

    fun fireMouseReleasedEvent(button: Int, x: Int, y: Int, shift: Boolean, ctrl: Boolean, alt: Boolean, numlock: Boolean) =
        mouseReleasedListeners.iterate { it(button, x, y, shift, ctrl, alt, numlock) }

    fun fireMouseClickedEvent(button: Int, x: Int, y: Int, shift: Boolean, ctrl: Boolean, alt: Boolean, numlock: Boolean) =
        mouseClickedListeners.iterate { it(button, x, y, shift, ctrl, alt, numlock) }

}











