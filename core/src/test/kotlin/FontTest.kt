import com.husker.minui.core.Font
import com.husker.minui.core.MinUI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FontTest {

    companion object {
        @BeforeAll @JvmStatic fun init(){
            MinUI.initialize()
        }
    }

    private lateinit var font: Font
    private val fontFamily = "Inter"
    private val fontTrademark = "Inter UI and Inter is a trademark of rsms."

    @BeforeEach
    fun setup(){
        font = Font.default
    }

    @Test
    fun newSize(){
        font.resize(10)
    }

    @Test
    fun family(){
        assertEquals(fontFamily, font.family)
    }

    @Test
    fun properties(){
        assertEquals(fontTrademark, font.trademark)
    }
}