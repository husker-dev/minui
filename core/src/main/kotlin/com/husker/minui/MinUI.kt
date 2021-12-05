package com.husker.minui

import com.husker.minuicore.MCore

fun wrapMainThread(invoke: () -> Unit) = MCore.bindMainThread(invoke)

class MinUI {
}