package com.husker.minui.natives

import com.husker.minui.core.Frame

abstract class PlatformLibrary(fileName: String) {

    companion object{
        val instance: PlatformLibrary
            get() = when(getOS()){
                "windows" -> Win
                else -> EmptyLibrary
            }

        private fun getOS(): String{
            val os = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> "windows"
                os.contains("nix") || os.contains("nux") || os.contains("aix") -> "linux"
                os.contains("mac") -> "macos"
                os.contains("sunos") -> "sunos"
                else -> "unknown"
            }
        }
    }

    init {
        if(fileName.isNotEmpty())
            LibraryUtils.loadResourceLibrary("/natives/libs/$fileName")
    }

    abstract fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean)
}