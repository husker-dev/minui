package examples

import com.husker.minui.core.MinUI
import com.husker.minui.core.MinUIEnvironment
import com.husker.minui.core.notification.*
import com.husker.minui.graphics.Image
import com.husker.minui.natives.LibraryUtils


var startTime = System.currentTimeMillis()

fun main(){
    LibraryUtils.forceLoad = true

    println(MinUIEnvironment.file)

    //val icon = Image.fromResource("icon.png")
    with(MinUI){
        appId = "minui.test"
        appName = "My Chat"
        appIcon = Image.fromResource("telephone.png")
    }

    val notification = Notification.create()
    if(notification is WinNotification) notification.build {
        type = ToastTypes.IncomingCall
        audio(loop = true)

        text("Doggy Doggo")
        text("Incoming Call")
        image(Image.fromResource("background.png"), crop = ImageCrop.Circle)

        action("Text reply", image= Image.fromResource("message.png"))
        action("Reminder", image= Image.fromResource("reminder.png"))
        action("Ignore", image= Image.fromResource("cancel.png"))
        action("Answer", image= Image.fromResource("telephone.png"))
    }
    notification.show()

    Thread.sleep(15000)
}

fun printDebug(title: String){
    println("\t= $title")
    println("\t| Time: \t${(System.currentTimeMillis() - startTime)}ms")
    startTime = System.currentTimeMillis()
    val a = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    println("\t| Mem: \t\t${a / 1024 / 1024} mb")
}