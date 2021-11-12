package com.husker.minui.core.listeners

import java.util.function.Consumer

interface MouseEventsReceiver {

    fun onMousePress(listener: Consumer<MouseEvent>)
    fun onMouseRelease(listener: Consumer<MouseEvent>)
    fun onMouseClick(listener: Consumer<MouseEvent>)
}