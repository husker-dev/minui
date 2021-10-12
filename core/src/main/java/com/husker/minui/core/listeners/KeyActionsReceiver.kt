package com.husker.minui.core.listeners

interface KeyActionsReceiver {

    fun addKeyPressListener(listener: (event: KeyEvent) -> Unit)
    fun addKeyReleasedListener(listener: (event: KeyEvent) -> Unit)
    fun addKeyTypedListener(listener: (event: KeyEvent) -> Unit)
}