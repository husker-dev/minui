package com.husker.minui.natives.impl.win

import com.husker.minui.core.MinUI
import com.husker.minui.core.notification.Notification
import com.husker.minui.core.notification.WinNotification
import com.husker.minui.natives.impl.win.Win.wideBytes
import kotlin.concurrent.thread

class WinNotificationExecutor { companion object {

    init{
        Win.nToastInit(
            "MinUI.${System.nanoTime()}".wideBytes,
            MinUI.appName.wideBytes,
            "C:\\Users\\redfa\\Desktop\\33a76a8509a87a8322f12b97ad4af0f3.jpg".wideBytes)

        Runtime.getRuntime().addShutdownHook(thread(start = false){
            Win.nToastClearAll()
            Win.nToastUninstall()
        })
    }

    fun showNotification(notification: Notification) {
        if(notification !is WinNotification)
            throw UnsupportedOperationException("Can't show not WinNotification instance")

        Win.nToastShow(notification.content.wideBytes)
    }
}}