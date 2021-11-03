package com.husker.minui.core.clipboard

import com.husker.minui.core.MinUIObject
import com.husker.minui.core.Resources
import com.husker.minui.graphics.Image
import com.husker.minui.natives.PlatformLibrary
import org.lwjgl.glfw.GLFW
import java.io.File

object Clipboard: MinUIObject() {

    val dataType: DataType
        get() = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.getClipboardDataType()
            else DataType.Text

    val dataKeys: Array<String>
        get() = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.getClipboardDataKeys()
        else emptyArray()

    var text: String?
        get() = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.getClipboardData(DataType.Text) as String?
            else GLFW.glfwGetClipboardString(Resources.window).orEmpty()
        set(value) = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.setClipboardData(DataType.Text, value!!)
            else GLFW.glfwSetClipboardString(Resources.window, value!!)

    var file: File?
        get() = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.getClipboardData(DataType.File) as File?
            else null
        set(value) = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.setClipboardData(DataType.File, value!!)
            else throw UnsupportedOperationException("File copying not supported in this OS")

    var image: Image?
        get() = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.getClipboardData(DataType.Image) as Image?
            else null
        set(value) = if(PlatformLibrary.isSupported()) PlatformLibrary.instance.setClipboardData(DataType.Image, value!!)
            else throw UnsupportedOperationException("Image copying not supported in this OS")

    fun getData(key: String): ByteArray?{
        return if(PlatformLibrary.isSupported()) PlatformLibrary.instance.getClipboardData(key)
        else null
    }

}