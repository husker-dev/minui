import com.husker.minui.core.MinUI
import com.husker.minui.core.popup.NativePopupMenu
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NativePopupTest {

    companion object {
        @BeforeAll
        @JvmStatic fun init(){
            MinUI.initialize()
        }
    }

    lateinit var popup: NativePopupMenu

    @BeforeEach
    fun setup(){
        popup = NativePopupMenu()
    }

    @Test
    fun showEmpty(){
        popup.showAsync(0, 0)
    }

    @Test
    fun button(){
        popup.button("TestButton")
        popup.showAsync(0, 0)
    }

    @Test
    fun subMenu(){
        popup.subMenu("SubMenu") {
            button()
        }
        popup.showAsync(0, 0)
    }

    @Test
    fun separator(){
        popup.separator()
        popup.showAsync(0, 0)
    }

    @Test
    fun mixed(){
        popup.button("Test")
        popup.separator()
        popup.subMenu("Sub menu"){
            button("Sub test")
            separator()
        }
        popup.showAsync(0, 0)
    }
}