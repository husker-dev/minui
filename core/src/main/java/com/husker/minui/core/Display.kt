package com.husker.minui.core

import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.geometry.Dimension
import com.husker.minui.geometry.Point
import org.lwjgl.glfw.GLFW.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Display(val id: Long): MinUIObject() {

    companion object{
        val default: Display
            get() = Display(glfwGetPrimaryMonitor())

        val displays: Array<Display>
            get() {
                val monitorsArray = glfwGetMonitors()!!
                val monitors = arrayListOf<Display>()
                for(i in 0 until monitorsArray.limit())
                    monitors.add(Display(monitorsArray[i]))
                return monitors.toTypedArray()
            }

        private val connectListeners = ConcurrentArrayList<(id: Display) -> Unit>()
        private val disconnectListeners = ConcurrentArrayList<(id: Display) -> Unit>()

        init{
            glfwSetMonitorCallback { monitorId, event ->
                if(event == GLFW_CONNECTED)
                    connectListeners.iterate { it.invoke(Display(monitorId)) }
                if(event == GLFW_DISCONNECTED)
                    disconnectListeners.iterate { it.invoke(Display(monitorId)) }
            }
        }

        fun addConnectListener(listener: (id: Display) -> Unit) {
            connectListeners.add(listener)
        }

        fun addDisconnectListener(listener: (id: Display) -> Unit) {
            disconnectListeners.add(listener)
        }
    }

    private val vidMode = glfwGetVideoMode(id)!!

    val width: Int
        get() = vidMode.width()

    val height: Int
        get() = vidMode.height()

    val refreshRate: Int
        get() = vidMode.refreshRate()

    val name: String
        get() = glfwGetMonitorName(id).orEmpty()

    val position: Point
        get() {
            val xBuf = IntBuffer.allocate(1)
            val yBuf = IntBuffer.allocate(1)
            glfwGetMonitorPos(id, xBuf, yBuf)
            return Point(xBuf[0].toDouble(), yBuf[0].toDouble())
        }

    val contentScale: Point
        get() {
            val xBuf = FloatBuffer.allocate(1)
            val yBuf = FloatBuffer.allocate(1)
            glfwGetMonitorContentScale(id, xBuf, yBuf)
            return Point(xBuf[0].toDouble(), yBuf[0].toDouble())
        }

    val physicalSize: Dimension
        get() {
            val xBuf = IntBuffer.allocate(1)
            val yBuf = IntBuffer.allocate(1)
            glfwGetMonitorPhysicalSize(id, xBuf, yBuf)
            return Dimension(xBuf[0].toDouble(), yBuf[0].toDouble())
        }

    val size: Dimension
        get() = Dimension(width.toDouble(), height.toDouble())

    fun addConnectListener(listener: () -> Unit) {
        Display.addConnectListener { if(it.id == id) listener.invoke() }
    }

    fun addDisconnectListener(listener: () -> Unit) {
        Display.addDisconnectListener { if(it.id == id) listener.invoke() }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Display

        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}