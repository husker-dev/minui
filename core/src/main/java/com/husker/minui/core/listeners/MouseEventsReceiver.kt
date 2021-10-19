package com.husker.minui.core.listeners

interface MouseEventsReceiver {

    fun addMousePressedListener(listener: (event: MouseEvent) -> Unit)
    fun addMouseReleasedListener(listener: (event: MouseEvent) -> Unit)
    fun addMouseClickedListener(listener: (event: MouseEvent) -> Unit)
}