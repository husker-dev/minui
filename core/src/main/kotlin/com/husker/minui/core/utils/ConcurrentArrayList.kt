package com.husker.minui.core.utils

class ConcurrentArrayList<T>(vararg elements: T): ArrayList<T>() {

    init{
        addAll(elements)
    }

    fun iterate(supplier: (T) -> Unit){
        val iterator = toMutableList().iterator()
        while(iterator.hasNext())
            supplier.invoke(iterator.next())
    }
}