package com.husker.minuicore.pipeline

abstract class MLPipeline(val name: String) {

    abstract val resourceFactory: MLResourceFactory

    abstract fun createWindow(): MLWindow

}