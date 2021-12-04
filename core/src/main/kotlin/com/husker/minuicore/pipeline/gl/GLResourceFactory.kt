package com.husker.minuicore.pipelines.gl

import com.husker.minuicore.MLResourceFactory
import com.husker.minuicore.MLTexture
import java.nio.ByteBuffer

class GLResourceFactory: MLResourceFactory() {

    init{

    }

    override fun createTexture(width: Int, height: Int, linear: Boolean): MLTexture {
        TODO("Not yet implemented")
    }

    override fun createTexture(width: Int, height: Int, byteBuffer: ByteBuffer, linear: Boolean): MLTexture {
        TODO("Not yet implemented")
    }

}