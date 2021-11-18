package com.husker.minui.layouts

class HorizontalPane: Pane() {

    override fun layout() {
        var x = 0.0
        children.iterate { child ->
            child.x = x
            child.y = 0.0
            child.width = child.preferredWidth
            child.height = height
            x += child.width
        }
    }
}