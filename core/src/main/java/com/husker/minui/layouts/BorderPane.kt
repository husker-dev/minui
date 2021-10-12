package com.husker.minui.layouts

import com.husker.minui.components.Component
import com.husker.minui.geometry.Point
import kotlin.math.max

open class BorderPane(): Pane() {

    companion object {
        const val Left = 0
        const val Right = 1
        const val Top = 2
        const val Bottom = 3
        const val Center = 4
    }

    private var _hgap = 5.0
    var hgap: Double
        get() = _hgap
        set(value) {
            _hgap = value
            layout()
        }

    private var _vgap = 5.0
    var vgap: Double
        get() = _vgap
        set(value) {
            _vgap = value
            layout()
        }

    private val components = hashMapOf<Int, Component>()

    constructor(component: Component): this(){
        add(component)
    }

    constructor(component: Component, hgap: Double, vgap: Double): this(hgap, vgap){
        add(component)
    }

    constructor(hgap: Double, vgap: Double): this(){
        this.hgap = hgap
        this.vgap = vgap
    }

    override fun layout() {
        val point1 = Point(0.0, 0.0)
        val point2 = Point(width, height)

        components[Top]?.let {
            point1.y += it.preferredHeight + vgap
            resizeComponent(it, 0.0, 0.0, width, it.preferredHeight)
        }
        components[Bottom]?.let {
            point2.y -= it.preferredHeight + vgap
            resizeComponent(it, 0.0, height - it.preferredHeight, width, it.preferredHeight)
        }
        components[Left]?.let {
            point1.x += it.preferredWidth + hgap
            resizeComponent(it, 0.0, point1.y, it.preferredWidth, point2.y - point1.y)
        }
        components[Right]?.let {
            point2.x -= it.preferredWidth + hgap
            resizeComponent(it, width - it.preferredWidth, point1.y, it.preferredWidth, point2.y - point1.y)
        }
        components[Center]?.let {
            resizeComponent(it, point1.x, point1.y, point2.x - point1.x, point2.y - point1.y)
        }
    }

    override fun add(component: Component) {
        addAt(component, Center)
    }

    private fun resizeComponent(component: Component, x: Double, y: Double, width: Double, height: Double){
        component.x = x
        component.y = y
        component.width = width
        component.height = height
    }

    override fun addAt(component: Component, at: Any){
        if(at is Number)
            addAt(component, at.toInt())
    }

    open fun addAt(component: Component, side: Int){
        components[side] = component
        children.clear()
        children.addAll(components.values.toTypedArray())
        layout()
    }


}