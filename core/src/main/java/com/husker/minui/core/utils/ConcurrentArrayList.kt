package com.husker.minui.core.utils

import java.util.stream.Collectors

class ConcurrentArrayList<T>: ArrayList<T>() {

    fun iterate(supplier: (T) -> Unit){
        val iterator = toMutableList().iterator()
        while(iterator.hasNext())
            supplier.invoke(iterator.next())
    }
}