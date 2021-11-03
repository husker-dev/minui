package com.husker.minui.natives.impl.win

class WinRegistry{ companion object{

    const val HKEY_CLASSES_ROOT = 0x80000000L
    const val HKEY_CURRENT_USER = 0x80000001L
    const val HKEY_LOCAL_MACHINE = 0x80000002L
    const val HKEY_USERS = 0x80000003L
    const val HKEY_PERFORMANCE_DATA = 0x80000004L
    const val HKEY_PERFORMANCE_TEXT = 0x80000050L
    const val HKEY_PERFORMANCE_NLSTEXT = 0x80000060L
    const val HKEY_CURRENT_CONFIG = 0x80000005L
    const val HKEY_DYN_DATA = 0x80000006L
    const val HKEY_CURRENT_USER_LOCAL_SETTINGS = 0x80000007L

    fun getFolderValuesMap(hkey: Long, path: String): Map<String, String>{
        val result = Win.nGetRegistryValues(hkey, path)
        if(result.size == 1){
            throw when(result[0].toString()){
                "1" -> UnsupportedOperationException("winerror: ERROR_INVALID_FUNCTION")
                "2" -> NullPointerException("Registry file not found: $path")
                "3" -> NullPointerException("Registry path not found: $path")
                "5" -> UnsupportedOperationException("Access denied: $path")
                else -> UnknownError("Unknown internal error.")
            }
        }
        val map = hashMapOf<String, String>()
        val keys = result[0] as Array<*>
        val values = result[1] as Array<*>

        for(i in keys.indices)
            map[keys[i] as String] = values[i] as String
        return map
    }

}}

