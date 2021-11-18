package com.husker.minui.layouts

class VerticalPane: Pane() {

    override fun layout() {
        var y = 0.0
        children.iterate { child ->
            child.x = 0.0
            child.y = y
            child.width = width
            child.height = child.preferredHeight
            y += child.height
        }
    }
}