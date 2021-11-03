package examples

import com.husker.minui.core.clipboard.Clipboard
import com.husker.minui.core.clipboard.ClipboardDataType
import com.husker.minui.natives.LibraryUtils


fun main(){
    LibraryUtils.forceLoad = true

    println(when (Clipboard.dataType) {
        ClipboardDataType.Text -> "Copied text: ${Clipboard.text}"
        ClipboardDataType.File -> "Copied file: ${Clipboard.file}"
        ClipboardDataType.Image -> with(Clipboard.image!!){ "Copied image size: $width x $height" }
        else -> "Unknown copied type"
    })

}