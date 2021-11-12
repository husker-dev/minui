import com.husker.minui.core.Display
import com.husker.minui.core.Frame
import com.husker.minui.core.MinUI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class FrameTest {

    companion object {
        @BeforeAll @JvmStatic fun init(){
            MinUI.initialize()
        }
    }

    private val exampleText = "Test"
    private lateinit var frame: Frame

    @BeforeEach
    fun setup(){
        frame = Frame()
    }

    @AfterEach
    fun close(){
        frame.close()
    }

    @Test
    fun title(){
        frame.title = exampleText
        frame.show()

        assertEquals(exampleText, frame.title)
    }

    @Test
    fun position(){
        frame.x = 100.0
        frame.y = 100.0
        frame.show()

        assertEquals(100, frame.x.toInt())
        assertEquals(100, frame.y.toInt())
    }

    @Test
    fun defaultDisplay(){
        val main = Display.default
        frame.show()

        assertEquals(main.id, frame.display.id)
    }

    @Test
    fun customDisplay(){
        val main = Display.displays.last()
        frame.show()

        assertEquals(main.id, frame.display.id)
    }

    @Test
    fun size(){
        frame.width = 100.0
        frame.height = 100.0
        frame.show()

        assertEquals(100, frame.width.toInt())
        assertEquals(100, frame.height.toInt())
    }

    @Test
    fun fullscreenCoordinates(){
        frame.state = Frame.FrameState.WindowedFullscreen
        frame.show()

        assertEquals(frame.display.position.x.toInt(), frame.x.toInt())
        assertEquals(frame.display.position.y.toInt(), frame.y.toInt())
    }

    @Test
    fun fullscreenSize(){
        frame.state = Frame.FrameState.WindowedFullscreen
        frame.show()

        assertEquals(frame.display.size.width.toInt(), frame.width.toInt())
        assertEquals(frame.display.size.height.toInt(), frame.height.toInt())
    }
}