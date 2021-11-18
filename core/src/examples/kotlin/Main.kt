
import com.husker.minui.components.ImageView
import com.husker.minui.core.Frame
import com.husker.minui.core.font.Font
import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Image
import com.husker.minui.layouts.FlowPane
import com.husker.minui.natives.LibraryUtils


fun main(){
    LibraryUtils.forceLoad = true

    val font = Font["lobster"]!!.resize(300)

    Frame().apply {
        vsync = false

        root = FlowPane().apply {
            add(ImageView(Image.fromResource("background1.png")).apply {
                preferredWidth = 600.0
                preferredHeight = 600.0
            })

            val image = Image.fromTexture(font.backend.getTextTexture("сыров"))
            add(ImageView(image, color = Color.Black).apply {
                preferredWidth = image.width.toDouble()
                preferredHeight = image.height.toDouble()
            })
        }
    }.show()

}
