package examples

import com.husker.minui.core.MinUI
import com.husker.minui.core.notification.Notification
import com.husker.minui.core.notification.Placement
import com.husker.minui.core.notification.WinNotification
import com.husker.minui.graphics.Image
import com.husker.minui.natives.LibraryUtils


var startTime = System.currentTimeMillis()

fun main(){
    LibraryUtils.forceLoad = true

    val icon = Image.fromResource("icon.png")
    with(MinUI){
        appId = "minui.test"
        appName = "MinUI Testing"
        appIcon = icon
    }

    val notification = Notification.create()
    if(notification is WinNotification) notification.build {
        text("Hello")
        image(icon, placement= Placement.Hero)
    }
    notification.show()

    Thread.sleep(10000)
}

fun printDebug(title: String){
    println("\t= $title")
    println("\t| Time: \t${(System.currentTimeMillis() - startTime)}ms")
    startTime = System.currentTimeMillis()
    val a = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    println("\t| Mem: \t\t${a / 1024 / 1024} mb")
}