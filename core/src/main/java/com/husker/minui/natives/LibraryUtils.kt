package com.husker.minui.natives

import com.husker.minui.core.MinUIEnvironment
import com.husker.minui.core.utils.IOUtils
import java.io.File
import java.io.FileOutputStream


class LibraryUtils {

    companion object {

        @JvmStatic
        var forceLoad = false

        @JvmStatic
        fun loadResourceLibrary(name: String){
            val fullName = "libraries/$name"
            val file = File(MinUIEnvironment.file, fullName)
            if(file.exists() && !forceLoad) {
                System.load(file.absolutePath)
                return
            }
            file.parentFile.mkdirs()
            IOUtils.copy(this::class.java.getResourceAsStream(name)!!, FileOutputStream(file))

            System.load(file.absolutePath)
        }
    }
}