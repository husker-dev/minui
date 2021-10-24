package com.husker.minui.core.listeners

interface MouseEventsReceiver {

    fun onMousePress(listener: (event: MouseEvent) -> Unit)
    fun onMouseRelease(listener: (event: MouseEvent) -> Unit)
    fun onMouseClick(listener: (event: MouseEvent) -> Unit)
}