package com.husker.minui.core.listeners

interface KeyEventsReceiver {

    fun onKeyPress(listener: (event: KeyEvent) -> Unit)
    fun onKeyRelease(listener: (event: KeyEvent) -> Unit)
    fun onKeyType(listener: (event: KeyEvent) -> Unit)
}