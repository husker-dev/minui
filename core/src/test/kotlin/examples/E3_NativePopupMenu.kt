package examples

import com.husker.minui.core.Frame
import com.husker.minui.core.Mouse
import com.husker.minui.core.popup.NativePopupMenu
import com.husker.minui.graphics.Color

fun main(){
    val frame = Frame("Native popup menu example")
    frame.vsync = false
    frame.visible = true

    val popup = NativePopupMenu {
        subMenu("Background"){
            button("White"){ frame.background = Color.White }
            separator()
            button("Red"){ frame.background = Color.Red }
            button("Blue"){ frame.background = Color.Blue }
            button("Coral"){ frame.background = Color.Coral }
            button("Aquamarine"){ frame.background = Color.Aquamarine }
            button("Aliceblue"){ frame.background = Color.Aliceblue }
            button("Khaki"){ frame.background = Color.Khaki }
        }
        subMenu("Title"){
            button("Hello world!") { frame.title = "Hello world!" }
            button("This is title bar") { frame.title = "This is title bar" }
            button("Русский заголовок") { frame.title = "Русский заголовок" }
        }
        separator()
        button("Hide window"){ frame.state = Frame.FrameState.Minimized }
        separator()
        button("Quit"){ frame.close() }
    }

    frame.onMousePress {
        popup.showAsync(Mouse.location, frame)
    }
}