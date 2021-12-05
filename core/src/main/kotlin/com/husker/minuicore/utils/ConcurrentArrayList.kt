package com.husker.minuicore.utils

fun <T> concurrentArrayList(vararg elements: T): ConcurrentArrayList<T>{
    return ConcurrentArrayList(*elements)
}

class ConcurrentArrayList<T>(vararg elements: T): ArrayList<T>() {

    init{
        addAll(elements)
    }

    fun iterate(supplier: (T) -> Unit){
        if(size == 0)
            return
        val iterator = toMutableList().iterator()
        while(iterator.hasNext())
            supplier.invoke(iterator.next())
    }
}