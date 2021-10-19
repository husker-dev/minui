package examples

import com.husker.minui.core.clipboard.Clipboard
import com.husker.minui.core.clipboard.DataType
import com.husker.minui.natives.LibraryUtils


fun main(){
    LibraryUtils.forceLoad = true

    println(when (Clipboard.dataType) {
        DataType.Text -> "Copied text: ${Clipboard.text}"
        DataType.File -> "Copied file: ${Clipboard.file}"
        DataType.Image -> with(Clipboard.image!!){ "Copied image size: $width x $height" }
        else -> "Unknown copied type"
    })

}