package examples

import com.husker.minui.core.Font
import com.husker.minui.core.Frame
import com.husker.minui.geometry.Dimension
import com.husker.minui.natives.LibraryUtils


var startTime = System.currentTimeMillis()

fun main(){
    LibraryUtils.forceLoad = true

    val frame = Frame("Font frame")
    frame.vsync = false
    frame.visible = true

}

fun printDebug(title: String){
    println("\t$title")
    println("\t| Time: \t${(System.currentTimeMillis() - startTime)}ms")
    startTime = System.currentTimeMillis()
    val a = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    println("\t| Mem: \t\t${a / 1024 / 1024} mb")
}