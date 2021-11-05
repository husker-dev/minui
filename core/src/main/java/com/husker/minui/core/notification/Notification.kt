package com.husker.minui.core.notification

import com.husker.minui.core.OS
import com.husker.minui.core.OS.Companion.Windows
import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.natives.PlatformLibrary


abstract class Notification {

    companion object {

        fun create(title: String = "", text: String = ""): Notification {
            val instance = when (OS.name) {
                Windows -> WinNotification()
                else -> throw NullPointerException()
            }
            instance.title = title
            instance.text = text
            return instance
        }

        fun createWithImage(title: String = "", text: String = "", src: String = ""): Notification {
            val instance = create(title, text)
            instance.configureImageNotification(src)
            return instance
        }
        fun createWithSmallImage(title: String = "", text: String = "", src: String = ""): Notification {
            val instance = create(title, text)
            instance.configureSmallNotification(src)
            return instance
        }
        fun createWithLargeImage(title: String = "", text: String = "", src: String = ""): Notification {
            val instance = create(title, text)
            instance.configureLargeImageNotification(src)
            return instance
        }

    }

    open var title = ""
    open var text = ""

    protected val onClickListeners = ConcurrentArrayList<Runnable>()

    fun show(){
        PlatformLibrary.instance.showNotification(this)
    }

    fun onClick(listener: Runnable){
        onClickListeners.add(listener)
    }

    abstract fun configureImageNotification(src: String)
    abstract fun configureLargeImageNotification(src: String)
    abstract fun configureSmallNotification(src: String)
}