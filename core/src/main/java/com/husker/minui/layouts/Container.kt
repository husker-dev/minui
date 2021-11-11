package com.husker.minui.layouts

import com.husker.minui.components.Component
import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.graphics.Graphics

abstract class Container(vararg children: Component): Component() {

    val children = ConcurrentArrayList(*children)

    open val childrenCount: Int
        get() = children.size

    override var width: Double
        get() = super.width
        set(value) {
            super.width = value
            layout()
        }

    override var height: Double
        get() = super.height
        set(value) {
            super.height = value
            layout()
        }

    open fun add(component: Component){
        addAt(component, childrenCount)
    }

    open fun addAt(component: Component, at: Any){
        if(at is Number){
            children.add(at.toInt(), component)
            component.parent = this
            layout()
        }
    }

    open fun layout(){
        children.iterate { child ->
            child.width = child.preferredWidth
            child.height = child.preferredHeight
        }
    }

    override fun draw(gr: Graphics) {
        children.iterate { child ->
            gr.translateX += child.x
            gr.translateY += child.y
            child.draw(gr)
            gr.translateX -= child.x
            gr.translateY -= child.y
        }

    }
}