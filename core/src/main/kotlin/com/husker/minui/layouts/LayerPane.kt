package com.husker.minui.layouts

import com.husker.minui.components.Component

class LayerPane(vararg components: Component): Pane(*components) {

    override fun layout() {
        children.iterate { child ->
            child.x = 0.0
            child.y = 0.0
            child.width = width
            child.height = height
        }
    }
}