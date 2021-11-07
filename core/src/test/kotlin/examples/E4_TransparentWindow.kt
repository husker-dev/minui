package examples

import com.husker.minui.components.ImageView
import com.husker.minui.core.Frame
import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Image
import com.husker.minui.layouts.BorderPane

fun main(){
    with(Frame("Transparent window example")){
        root = BorderPane()
        background = Color.Transparent
        visible = true
        vsync = false

        root.add(ImageView(Image.fromResource("/background.png")))
    }
}