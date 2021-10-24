package examples

import com.husker.minui.core.*
import com.husker.minui.graphics.*
import com.husker.minui.layouts.BorderPane
import com.husker.minui.natives.LibraryUtils
import kotlin.concurrent.timer
import kotlin.system.exitProcess


fun main(){
    LibraryUtils.forceLoad = true

    Frame("MinUI Application").apply {
        root = BorderPane()
        //background = Color.Transparent
        visible = true
        vsync = false


    }

}