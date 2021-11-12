package com.husker.minui.natives.impl.win

import com.husker.minui.core.MinUI
import com.husker.minui.core.notification.Notification
import com.husker.minui.core.notification.WinNotification
import com.husker.minui.natives.impl.win.Win.wideBytes
import kotlin.concurrent.thread

class WinNotificationExecutor { companion object {

    init{
        val iconPath = if(MinUI.appIcon != null){
            MinUI.appIcon!!.cacheFile().absolutePath
        }else ""

        Win.nToastInit(
            "MinUI.${MinUI.appId}".wideBytes,
            MinUI.appName.wideBytes,
            iconPath.wideBytes)

        Runtime.getRuntime().addShutdownHook(thread(start = false){
            Win.nToastClearAll()
            //Win.nToastUninstall()
        })
    }

    fun showNotification(notification: Notification) {
        if(notification !is WinNotification)
            throw UnsupportedOperationException("Can't show not WinNotification instance")

        Win.nToastShow(notification.content.wideBytes)
    }
}}