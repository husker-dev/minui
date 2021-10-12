package com.husker.minui.layouts

import com.husker.minui.components.Component

open class FlowPane(): Pane() {

    enum class VPos{
        Top, Center, Bottom
    }

    enum class HPos{
        Left, Center, Right
    }

    private var _vPos = VPos.Top
    var vPos: VPos
        get() = _vPos
        set(value) {
            _vPos = value
            layout()
        }

    private var _hPos = HPos.Center
    var hPos: HPos
        get() = _hPos
        set(value) {
            _hPos = value
            layout()
        }

    private var _vgap = 5.0
    var vgap: Double
        get() = _vgap
        set(value) {
            _vgap = value
            layout()
        }

    private var _hgap = 5.0
    var hgap: Double
        get() = _hgap
        set(value) {
            _hgap = value
            layout()
        }

    constructor(hgap: Double, vgap: Double): this(){
        this.vgap = vgap
        this.hgap = hgap
    }

    constructor(gap: Double): this(){
        this.hgap = gap
        this.vgap = gap
    }

    override fun layout() {
        val lines = cutByLines(children)

        val allHeight = lines.sumOf { line -> line.maxOf { it.preferredHeight } } + vgap * (lines.size - 1)
        var currentY = when (vPos){
            VPos.Top -> 0.0
            VPos.Center -> (height - allHeight) / 2.0
            VPos.Bottom -> height - allHeight
        }

        lines.forEachIndexed { lineIndex, line ->
            val lineWidth = line.sumOf { it.preferredWidth } + hgap * (line.size - 1)
            val lineHeight = line.maxOf { it.preferredHeight }

            var currentX = when (hPos){
                HPos.Left -> 0.0
                HPos.Center -> (width - lineWidth) / 2.0
                HPos.Right -> width - lineWidth
            }

            line.forEachIndexed { componentIndex, component ->
                component.x = currentX
                component.y = currentY
                component.width = component.preferredWidth
                component.height = component.preferredHeight
                currentX += component.preferredWidth
                if(componentIndex < line.size - 1)
                    currentX += hgap
            }
            currentY += lineHeight
            if(lineIndex < lines.size - 1)
                currentY += vgap
        }
    }

    private fun cutByLines(components: List<Component>): List<List<Component>>{
        val lines = arrayListOf<List<Component>>()

        var currentLine = arrayListOf<Component>()
        var currentX = 0.0

        components.forEach{ child ->
            when {
                currentX == 0.0 -> {
                    currentX = child.preferredWidth
                    currentLine.add(child)
                }
                currentX + hgap + child.preferredWidth <= width -> {
                    currentX += hgap + child.preferredWidth
                    currentLine.add(child)
                }
                else -> {
                    lines.add(currentLine)
                    currentLine = arrayListOf(child)
                    currentX = child.preferredWidth
                }
            }
        }
        if(currentLine.isNotEmpty())
            lines.add(currentLine)
        return lines
    }
}