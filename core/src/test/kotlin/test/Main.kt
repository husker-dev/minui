package test

import com.husker.minui.components.*
import com.husker.minui.core.*
import com.husker.minui.geometry.Point
import com.husker.minui.graphics.*
import com.husker.minui.layouts.BorderPane


fun main(){
    val size = Display.default.height / 2.5

    val frame = Frame("MinUI Application", size, size)
    with(frame){
        position = Point(size, size)
        root = BorderPane()
        background = Color.Transparent
        visible = true

        val image = Image.fromResourceFile("/bg.png")
        root.add(ImageView(image))
        icon = image
    }
}