package com.husker.minuicore.pipeline.gl

val GL_COLOR_BUFFER_BIT = 0x4000
val GL_DEPTH_BUFFER_BIT = 0x100

external fun glClear(mask: Int)
external fun glClearColor(r: Float, g: Float, b: Float, a: Float,)