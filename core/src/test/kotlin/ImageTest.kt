import com.husker.minui.core.MinUI
import com.husker.minui.core.utils.BufferUtils
import com.husker.minui.graphics.Image
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

class ImageTest {

    companion object {
        @BeforeAll @JvmStatic fun init(){
            MinUI.initialize()
        }
    }

    // Java icon from Wikipedia
    private val imageURL = "https://upload.wikimedia.org/wikipedia/ru/thumb/3/39/Java_logo.svg/43px-Java_logo.svg.png"
    private val imageResourcePath = "/java_icon.png"

    @Test
    fun fromURL(){
        Image.fromURL(imageURL)
    }

    @Test
    fun fromResources(){
        Image.fromResource(imageResourcePath)
    }

    @Test
    fun fromBytes(){
        val bytes = this::class.java.getResourceAsStream(imageResourcePath)!!.readBytes()
        Image.fromBytes(bytes)
    }

    @Test
    fun fromByteBuffer(){
        val bytes = this::class.java.getResourceAsStream(imageResourcePath)!!.readBytes()
        val buffer = ByteBuffer.allocateDirect(bytes.size)
        buffer.put(bytes)
        BufferUtils.flipBuffer(buffer)

        Image.fromByteBuffer(buffer)
    }

    @Test
    fun resizeSmall(){
        val image = Image.create(100, 100)
        image.resize(10, 10)
    }

    @Test
    fun resizeMedium(){
        val image = Image.create(100, 100)
        image.resize(1000, 1000)
    }

    @Test
    fun resizeLarge(){
        val image = Image.create(100, 100)
        image.resize(10000, 10000)
    }
}