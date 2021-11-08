package examples

import com.husker.minui.components.ImageView
import com.husker.minui.core.Frame
import com.husker.minui.graphics.Image
import com.husker.minui.graphics.ImageEncoding
import com.husker.minui.graphics.ResizeType
import com.husker.minui.layouts.FlowPane
import com.husker.minui.natives.LibraryUtils


var startTime = System.currentTimeMillis()

fun main(){
    LibraryUtils.forceLoad = true

    val icon = Image.fromResource("icon.png")

    Frame().apply {
        vsync = false
        root = FlowPane().apply {
            printDebug("Init")
            for(type in ResizeType.values()){
                val scaled = icon.resize(100, 100, type= type)
                printDebug("Scale: ${type.name}")
                scaled.linearFiltering = false

                val view = ImageView(scaled)
                view.preferredWidth = 500.toDouble()
                view.preferredHeight = 500.toDouble()
                add(view)
            }
        }
        visible = true
    }

}

fun printPixels(image: Image){
    val data = image.data
    for(i in 0..10){
        val index = image.components * i
        val r = data.get(index)
        val g = data.get(index+1)
        val b = data.get(index+2)
        val a = data.get(index+3)
        println("${r.toUByte()} ${g.toUByte()} ${b.toUByte()} ${a.toUByte()}")
    }
}

fun printDebug(title: String){
    println("\t= $title")
    println("\t| Time: \t${(System.currentTimeMillis() - startTime)}ms")
    startTime = System.currentTimeMillis()
    val a = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    println("\t| Mem: \t\t${a / 1024 / 1024} mb")
}