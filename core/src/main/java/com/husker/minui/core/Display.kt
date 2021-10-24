package com.husker.minui.core

import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.geometry.Dimension
import com.husker.minui.geometry.Point
import com.husker.minui.geometry.Rectangle
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryStack.stackPush


class Display(val id: Long): MinUIObject() {

    companion object{
        val default: Display
            get() = MinUI.invokeLaterSync { Display(glfwGetPrimaryMonitor()) }!!

        val displays: Array<Display>
            get() = MinUI.invokeLaterSync {
                val monitorsArray = glfwGetMonitors()!!
                val monitors = arrayListOf<Display>()
                for(i in 0 until monitorsArray.limit())
                    monitors.add(Display(monitorsArray[i]))
                monitors.toTypedArray()
            }!!

        private val connectListeners = ConcurrentArrayList<(id: Display) -> Unit>()
        private val disconnectListeners = ConcurrentArrayList<(id: Display) -> Unit>()

        init{
            MinUI.invokeLaterSync {
                glfwSetMonitorCallback { monitorId, event ->
                    if(event == GLFW_CONNECTED)
                        connectListeners.iterate { it.invoke(Display(monitorId)) }
                    if(event == GLFW_DISCONNECTED)
                        disconnectListeners.iterate { it.invoke(Display(monitorId)) }
                }
            }
        }

        fun addConnectListener(listener: (id: Display) -> Unit) {
            connectListeners.add(listener)
        }

        fun addDisconnectListener(listener: (id: Display) -> Unit) {
            disconnectListeners.add(listener)
        }

        fun ofPoint(point: Point): Display? {
            for(display in displays) {
                if (display.bounds.contains(point))
                    return display
            }
            return null
        }

        fun ofFrame(frame: Frame): Display = MinUI.invokeLaterSync {
            if(frame.state == Frame.FrameState.Fullscreen || frame.state == Frame.FrameState.WindowedFullscreen)
                Display(glfwGetWindowMonitor(frame.backend.window))
            else {
                // TODO: Доделать это...
                val position = frame.position
                val size = frame.size
                println(size)
                ofPoint(Point(position.x + size.width / 2, position.y + size.height / 2))
            }
        }!!

    }

    private val vidMode = MinUI.invokeLaterSync { glfwGetVideoMode(id)!! }!!

    val width: Int
        get() = vidMode.width()

    val height: Int
        get() = vidMode.height()

    val refreshRate: Int
        get() = vidMode.refreshRate()

    val name: String
        get() = MinUI.invokeLaterSync { glfwGetMonitorName(id) }.orEmpty()

    val position: Point
        get() = MinUI.invokeLaterSync {
            stackPush().use { stack ->
                val sx = stack.mallocInt(1)
                val sy = stack.mallocInt(1)
                glfwGetMonitorPos(id, sx, sy)
                Point(sx[0].toDouble(), sy[0].toDouble())
            }
        }!!

    val contentScale: Dimension
        get() = MinUI.invokeLaterSync {
            stackPush().use { stack ->
                val sx = stack.mallocFloat(1)
                val sy = stack.mallocFloat(1)
                glfwGetMonitorContentScale(id, sx, sy)
                Dimension(sx[0].toDouble(), sy[0].toDouble())
            }
        }!!

    val physicalSize: Dimension
        get() = MinUI.invokeLaterSync {
            stackPush().use { stack ->
                val sx = stack.mallocInt(1)
                val sy = stack.mallocInt(1)
                glfwGetMonitorPhysicalSize(id, sx, sy)
                Dimension(sx[0].toDouble(), sy[0].toDouble())
            }
        }!!

    val size: Dimension
        get() = Dimension(width.toDouble(), height.toDouble())

    val bounds: Rectangle
        get() {
            val pos = position
            val size = size
            return Rectangle(pos.x, pos.y, size.width, size.height)
        }

    val isPrimary: Boolean
        get() = this == default

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