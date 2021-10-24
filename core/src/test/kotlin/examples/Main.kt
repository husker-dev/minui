package examples

import com.husker.minui.core.*
import com.husker.minui.core.Frame.*


fun main(){
    val frame = Frame("MinUI Application")
    with(frame){
        vsync = false
        closeOperation = CloseOperation.ExitProgram
        visible = true
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