package com.husker.minui.natives

import com.husker.minui.core.Frame

object EmptyLibrary: PlatformLibrary("") {
    override fun setTaskbarIconEnabled(frame: Frame, enabled: Boolean) {

    }
}