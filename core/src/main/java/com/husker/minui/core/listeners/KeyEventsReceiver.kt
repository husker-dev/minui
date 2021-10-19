package com.husker.minui.core.listeners

interface KeyEventsReceiver {

    fun addKeyPressedListener(listener: (event: KeyEvent) -> Unit)
    fun addKeyReleasedListener(listener: (event: KeyEvent) -> Unit)
    fun addKeyTypedListener(listener: (event: KeyEvent) -> Unit)
}