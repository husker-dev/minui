package com.husker.minuicore.pipeline

abstract class MPipeline(val name: String) {

    abstract val resourceFactory: MResourceFactory

    abstract fun createWindow(): MWindow

}