package examples

import com.husker.minui.core.*
import com.husker.minui.geometry.Dimension
import com.husker.minui.graphics.*
import com.husker.minui.layouts.BorderPane
import com.husker.minui.natives.LibraryUtils
import com.husker.minui.natives.platform.PlatformLibrary
import org.lwjgl.glfw.GLFW
import java.nio.IntBuffer


fun main(){
    LibraryUtils.forceLoad = true
    val frame = Frame("MinUI Application")
    with(frame){
        root = BorderPane()
        background = Color.Transparent
        visible = true
        vsync = false

    }

    while(true){
        for(display in Display.displays) {
            println("Monitor: ${display.id}")
            println("\tpsize: \t${display.physicalSize}")
            println("\tscale: \t${display.contentScale}")
            println("\tpos: \t${display.position}")
            println("\tname: \t${display.name}")
            println("\tsize: \t${display.size}")
            println("\tfps: \t${display.refreshRate}")
            println()
        }

        Thread.sleep(1000)
    }
}