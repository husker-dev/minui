package com.husker.minuicore.platform

import com.husker.minuicore.utils.concurrentArrayList
import java.nio.charset.StandardCharsets

const val x64 = "x64"
const val x86 = "x86"
const val arm = "arm"
const val arm64 = "arm64"

val ByteArray.c_type: ByteArray
    get() {
        val cBytes = ByteArray(this.size + 1)
        System.arraycopy(this, 0, cBytes, 0, this.size)
        return cBytes
    }
val String.c_str: ByteArray
    get() = this.bytes.c_type
val String.bytes: ByteArray
    get() = toByteArray(StandardCharsets.UTF_8)

abstract class MLPlatform(val name: String) {

    companion object {

    }

    private val themeListeners = concurrentArrayList<(Boolean) -> Unit>()
    abstract val architecture: String
    abstract val isDarkTheme: Boolean

    abstract fun createWindowManager(): MWindowManager

    fun createWindowManager(handle: Long): MWindowManager {
        val manager = createWindowManager()
        manager.bindHandle(handle)
        return manager
    }

    protected fun fireThemeChangedEvent(isDark: Boolean){
        themeListeners.iterate { it.invoke(isDark) }
    }

    fun removeThemeChangedListener(listener: (isDark: Boolean) -> Unit){
        themeListeners.remove(listener)
    }

    fun onThemeChanged(listener: (isDark: Boolean) -> Unit){
        themeListeners.add(listener)
    }
}