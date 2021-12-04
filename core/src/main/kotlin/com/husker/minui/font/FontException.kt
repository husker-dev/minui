package com.husker.minui.core.font

class FontException(message: String, val status: Int, val addition: String?): Exception("$message (Error code: $status, Addition info: ${addition ?: ""})")