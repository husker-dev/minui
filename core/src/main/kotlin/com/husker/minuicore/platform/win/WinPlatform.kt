package com.husker.minuicore.platform.win


import com.husker.minuicore.MCore
import com.husker.minuicore.platform.MLPlatform
import com.husker.minuicore.platform.*
import java.nio.charset.StandardCharsets

val String.wideBytes: ByteArray
    get() = bytes.wideBytes
val ByteArray.wideBytes: ByteArray
    get() = String(this, StandardCharsets.UTF_8).toByteArray(StandardCharsets.UTF_16LE)
val ByteArray.utf8Bytes: ByteArray
    get() = String(this, StandardCharsets.UTF_16LE).toByteArray(StandardCharsets.UTF_8)
val ByteArray.utf8Text: String
    get() = String(this, StandardCharsets.UTF_16LE)
val ByteArray.c_wtype: ByteArray
    get() {
        val cBytes = ByteArray(this.size + 2)
        System.arraycopy(this, 0, cBytes, 0, this.size)
        return cBytes
    }

class WinPlatform: MLPlatform("Windows") {

    companion object {
        init{
            MCore.loadLibrary("minui_natives/windows/${MCore.platform.architecture}/win.dll")
        }
    }

    override val architecture: String by lazy{
            val env = System.getenv()
            if("PROCESSOR_ARCHITECTURE" in env){
                return@lazy when(env["PROCESSOR_ARCHITECTURE"]){
                    "AMD64", "IA64" -> x64
                    "ARM64" -> arm64
                    "X86" -> x86
                    else -> arm
                }
            }else{
                val cmdResult = Runtime.getRuntime().exec("cmd /c wmic OS get OSArchitecture")
                    .inputStream
                    .readBytes()
                    .toString(StandardCharsets.UTF_8)
                return@lazy when{
                    "64" in cmdResult -> x64
                    "32" in cmdResult -> x86
                    else -> arm64
                }
            }
        }

    override fun createWindowManager() = WinWindowManager()

}