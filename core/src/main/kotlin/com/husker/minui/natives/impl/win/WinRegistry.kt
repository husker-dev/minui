package com.husker.minui.natives.impl.win

import com.husker.minui.natives.impl.win.Win.wideBytes

enum class HKey(val value: Long){
    HKEY_CLASSES_ROOT(0x80000000L),
    HKEY_CURRENT_USER(0x80000001L),
    HKEY_LOCAL_MACHINE(0x80000002L),
    HKEY_USERS(0x80000003L),
    HKEY_PERFORMANCE_DATA(0x80000004L),
    HKEY_PERFORMANCE_TEXT(0x80000050L),
    HKEY_PERFORMANCE_NLSTEXT(0x80000060L),
    HKEY_CURRENT_CONFIG(0x80000005L),
    HKEY_DYN_DATA(0x80000006L),
    HKEY_CURRENT_USER_LOCAL_SETTINGS(0x80000007L)
}

class WinRegistry{ companion object{

    fun getFolderValuesMap(hkey: HKey, path: String): Map<String, String>{
        val result = Win.nRegistryGetMap(hkey.value, path.wideBytes)
        if(result.size == 1){
            throw when(result[0].toString()){
                "1" -> UnsupportedOperationException("winerror: ERROR_INVALID_FUNCTION")
                "2" -> NullPointerException("Registry file not found: ${hkey.name}\\$path")
                "3" -> NullPointerException("Registry path not found: ${hkey.name}\\$path")
                "5" -> UnsupportedOperationException("Access denied: ${hkey.name}\\$path")
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

