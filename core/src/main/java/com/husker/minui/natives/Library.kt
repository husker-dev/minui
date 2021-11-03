package com.husker.minui.natives

import com.husker.minui.core.MinUIObject
import com.husker.minui.core.OS

open class Library(vararg libraries: String): MinUIObject() {

    init {
        val archName = when(OS.arch){
            OS.X64 -> "64"
            OS.X86 -> "86"
            else -> "_${OS.arch.lowercase()}"
        }
        val filePostfix = when(OS.name){
            OS.Windows -> ".dll"
            OS.Linux, OS.MacOS -> ".so"
            else -> ""
        }
        for(library in libraries)
            if(library.isNotEmpty()) LibraryUtils.loadResourceLibrary("/com/husker/minui/natives/impl/${OS.shortName + archName}/${library + filePostfix}")
    }
}