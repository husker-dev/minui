
import com.husker.minui.components.FitType
import com.husker.minui.components.ImageView
import com.husker.minui.core.Frame
import com.husker.minui.core.font.Font
import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Image
import com.husker.minui.layouts.FlowPane
import com.husker.minui.natives.LibraryUtils


fun main(){
    LibraryUtils.forceLoad = true

    val font = Font["times new roman"]!!.resize(1000)

    Frame().apply {
        vsync = false
        root = FlowPane().apply {
            for(char in "Hello world"){
                val image = font.getGlyph(char).getImage()

                add(ImageView(image, color = Color.Black, fitType = FitType.Cover).apply {
                    preferredWidth = image.width.toDouble()
                    preferredHeight = image.height.toDouble()
                })
            }
        }
    }.show()

}
