
import com.husker.minui.components.ImageView
import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.font.Font
import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Image
import com.husker.minui.layouts.VerticalPane
import com.husker.minui.natives.LibraryUtils


fun main(){
    LibraryUtils.forceLoad = true

    val display = Display.default

    Frame().apply {
        vsync = false

        root = VerticalPane().apply {
            val time = System.currentTimeMillis()
            for(i in 10..40 step(2)){
                val font = Font["times new roman", i]
                val image = Image.fromTexture(font.backend.getTextTexture("${i}pt: Hello World", display.dpi))
                add(ImageView(image, color = Color.Black).apply {
                    preferredWidth = image.width.toDouble()
                    preferredHeight = image.height.toDouble()
                })
            }
            println("Init: ${System.currentTimeMillis() - time}ms")
        }
    }.show()

}
