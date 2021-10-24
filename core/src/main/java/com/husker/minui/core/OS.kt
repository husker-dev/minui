package com.husker.minui.core

class OS {

    companion object {
        const val Windows = "windows"
        const val MacOS = "macos"
        const val Linux = "linux"
        const val SunOS = "sunos"

        fun get(): String {
            val os = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> Windows
                os.contains("nix") || os.contains("nux") || os.contains("aix") -> Linux
                os.contains("mac") -> MacOS
                os.contains("sunos") -> SunOS
                else -> os
            }
        }

        fun isWindows(): Boolean = get() == Windows
        fun isLinux(): Boolean = get() == Linux
        fun isMacOS(): Boolean = get() == MacOS
        fun isSunOS(): Boolean = get() == SunOS
    }
}