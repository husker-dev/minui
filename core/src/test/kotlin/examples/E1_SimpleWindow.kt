package examples

import com.husker.minui.components.Button
import com.husker.minui.core.Frame

fun main(){
    val frame = Frame("Simple frame example")
    frame.root.add(Button("Button"))
    frame.visible = true
}