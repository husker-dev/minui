package com.husker.minuicore.utils

var time = -1L
fun timeStump(){
    val newTime = System.currentTimeMillis()
    if(time != -1L)
        println("Time: ${(newTime - time) / 1000.0} sec")
    time = newTime
}

fun resetTime(){
    time = System.currentTimeMillis()
}