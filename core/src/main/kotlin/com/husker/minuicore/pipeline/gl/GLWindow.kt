package com.husker.minuicore.pipelines.gl

import com.husker.minuicore.MLCore
import com.husker.minuicore.MLWindow

class GLWindow: MLWindow() {

    init{
        MLCore.windows.add(this)
    }
}