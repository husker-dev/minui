package examples

import com.husker.minui.core.notification.*
import com.husker.minui.natives.LibraryUtils


var startTime = System.currentTimeMillis()

fun main(){
    LibraryUtils.forceLoad = true

    val imgPath = "C:\\Users\\redfa\\Desktop\\cutest-baby-cheetah-jokes-funny-baby-animals-memes-baby-animals.jpg"
    Notification.createWithImage("Title", "Text", imgPath).show()

    /*
    // Если уведомление для винды
    if(notification is WinNotification) notification.build {
        text("Русский кот")
        text("Кто я?")
        image(imgPath, placement= Placement.AppLogo, crop= ImageCrop.Circle, query= true)

        action("Ответить")
    }.show()

     */
}

fun printDebug(title: String){
    println("\t$title")
    println("\t| Time: \t${(System.currentTimeMillis() - startTime)}ms")
    startTime = System.currentTimeMillis()
    val a = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    println("\t| Mem: \t\t${a / 1024 / 1024} mb")
}