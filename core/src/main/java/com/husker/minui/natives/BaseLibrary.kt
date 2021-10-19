package com.husker.minui.natives

import java.nio.ByteBuffer

object BaseLibrary {

    init {
        LibraryUtils.loadResourceLibrary("/natives/libs/base.dll")
    }

    external fun fillDirectBuffer(buffer: ByteBuffer, bytes: ByteArray)

}