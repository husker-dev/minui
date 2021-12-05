package com.husker.minuicore.platform

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
val String.bytes: ByteArray
    get() = toByteArray(StandardCharsets.UTF_8)

abstract class MLPlatform(val name: String) {

    companion object {

    }

    abstract val architecture: String

    abstract fun createWindowManager(): MWindowManager

    fun createWindowManager(handle: Long): MWindowManager {
        val manager = createWindowManager()
        manager.bindHandle(handle)
        return manager
    }
}