package com.husker.minui.core

import java.nio.charset.StandardCharsets

class OS {

    companion object {
        @JvmStatic val Windows = "windows"
        @JvmStatic val MacOS = "macos"
        @JvmStatic val Linux = "linux"
        @JvmStatic val SunOS = "sunos"

        @JvmStatic val X64 = "x64"
        @JvmStatic val X86 = "x86"
        @JvmStatic val Arm = "arm"

        val name: String
            get() {
                val os = System.getProperty("os.name").lowercase()
                return when {
                    os.contains("win") -> Windows
                    os.contains("nix") || os.contains("nux") || os.contains("aix") -> Linux
                    os.contains("mac") -> MacOS
                    os.contains("sunos") -> SunOS
                    else -> os
                }
            }

        val shortName: String
            get() = when(name){
                Windows -> "win"
                else -> name
            }

        fun isWindows(): Boolean = name == Windows
        fun isLinux(): Boolean = name == Linux
        fun isMacOS(): Boolean = name == MacOS
        fun isSunOS(): Boolean = name == SunOS

        // TODO: Here is missing Arm and x86 tests
        val arch: String
            get(){
                return if(isWindows()){
                    val cmdResult = Runtime.getRuntime().exec("cmd /c wmic OS get OSArchitecture")
                        .inputStream
                        .readBytes()
                        .toString(StandardCharsets.UTF_8)
                    return when{
                        "64" in cmdResult -> X64
                        "32" in cmdResult -> X86
                        else -> Arm
                    }
                }else
                    if(System.getProperty("os.arch").contains("64")) X64 else X86
            }

        fun isX64() = arch == X64
        fun isX86() = arch == X86
        fun isArm() = arch == Arm
    }
}