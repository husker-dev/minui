package com.husker.minui.layouts

class LayerPane: Pane() {

    override fun layout() {
        children.iterate { child ->
            child.x = 0.0
            child.y = 0.0
            child.width = width
            child.height = height
        }
    }
}