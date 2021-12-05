package com.husker.minuicore.utils

class Trigger {

    private var ready = false
    private val notifier = Object()

    fun ready(){
        ready = true
        synchronized(notifier) { notifier.notifyAll() }
    }

    fun waitForReady(){
        if(!ready)
            synchronized(notifier) { notifier.wait() }
    }

    fun reset(): Trigger {
        ready = false
        return this
    }
}