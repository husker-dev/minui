package com.husker.minui.core.utils

import java.io.InputStream
import java.io.OutputStream

class IOUtils { companion object {

    fun copy(input: InputStream, output: OutputStream, buffer: Int = 8192){
        val bufferBytes = ByteArray(buffer)
        var read: Int
        while (input.read(bufferBytes).also { read = it } != -1)
            output.write(bufferBytes, 0, read)

        input.close()
        output.close()
    }
}}