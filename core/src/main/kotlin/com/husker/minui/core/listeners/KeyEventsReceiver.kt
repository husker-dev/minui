package com.husker.minui.core.listeners

import java.util.function.Consumer

interface KeyEventsReceiver {

    fun onKeyPress(listener: Consumer<KeyEvent>)
    fun onKeyRelease(listener: Consumer<KeyEvent>)
    fun onKeyType(listener: Consumer<KeyEvent>)
}