
import com.husker.minui.wrapMainThread
import com.husker.minuicore.MCore
import com.husker.minuicore.utils.resetTime
import com.husker.minuicore.utils.timeStump

fun main(){
    resetTime()
    MCore.forceLibraryLoad = true
    wrapMainThread {
        val window = MCore.pipeline.createWindow()
        //window.title = "My beautiful window"
        //window.position = Pair(400, 400)
        //window.size = Pair(500, 500)
        //window.background = Triple(0.8f, 0.8f, 0.8f)
        window.visible = true
        window.title = "OpenGL Window"
        window.vsync = false
        timeStump()

        Thread.sleep(1000)
        //window.close()
        //MLCore.pipeline.createWindow().visible = true

        //MLCore.pipeline.createWindow().visible = true

    }
}




