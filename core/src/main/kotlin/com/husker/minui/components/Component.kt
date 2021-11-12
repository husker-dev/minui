package com.husker.minui.components

import com.husker.minui.layouts.Container
import com.husker.minui.core.Positionable
import com.husker.minui.core.Sizable
import com.husker.minui.geometry.Dimension
import com.husker.minui.geometry.Point
import com.husker.minui.core.Drawable
import com.husker.minui.core.utils.ConcurrentArrayList

abstract class Component: Drawable, Sizable, Positionable {

    var preferredWidth = 0.0
    var preferredHeight = 0.0
    var preferredSize: Dimension
        get() = Dimension(preferredWidth, preferredHeight)
        set(value) {
            preferredWidth = value.width
            preferredHeight = value.height
        }

    private var _x = 0.0
    override var x: Double
        get() = _x
        set(value) {
            _x = value
            _componentMovedListeners.iterate { it.run() }
        }

    private var _y = 0.0
    override var y: Double
        get() = _y
        set(value) {
            _y = value
            _componentMovedListeners.iterate { it.run() }
        }

    private var _width = 0.0
    override var width: Double
        get() = _width
        set(value) {
            _width = value
            _componentResizedListeners.iterate { it.run() }
        }

    private var _height = 0.0
    override var height: Double
        get() = _height
        set(value) {
            _height = value
            _componentResizedListeners.iterate { it.run() }
        }

    override var size: Dimension
        get() = Dimension(x, y)
        set(value) {
            width = value.width
            height = value.height
        }

    override var position: Point
        get() = Point(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    var parent: Container? = null

    val framePosition: Point
        get() {
            if(parent == null)
                return Point(x, y)
            val parentPos = parent!!.framePosition
            return Point(parentPos.x + x, parentPos.y + y)
        }

    private var _componentResizedListeners = ConcurrentArrayList<Runnable>()
    private var _componentMovedListeners = ConcurrentArrayList<Runnable>()

    protected fun isZeroSize(): Boolean = width <= 0.0 || height <= 0.0

    override fun toString(): String {
        return "${this::class.java.canonicalName}(x=$x, y=$y, prefW=$preferredWidth, prefH=$preferredHeight, width=$width, height=$height)"
    }

    override fun onResize(listener: Runnable) {
        _componentResizedListeners.add(listener)
    }

    override fun onMoved(listener: Runnable) {
        _componentMovedListeners.add(listener)
    }
}