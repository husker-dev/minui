package com.husker.minuicore.transform

import kotlin.math.sqrt


class Matrix(
    vararg var elements: Float,
    val width: Int = sqrt(elements.size.toDouble()).toInt(),
    val height: Int = sqrt(elements.size.toDouble()).toInt()
) {

    companion object {
        fun identity() = Matrix(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )

        fun scale(sx: Float, sy: Float, sz: Float) = Matrix(
            sx, 0f, 0f, 0f,
            0f, sy, 0f, 0f,
            0f, 0f, sz, 0f,
            0f, 0f, 0f, 1f
        )

        fun translate(tx: Float, ty: Float, tz: Float) = Matrix(
            1f, 0f, 0f, tx,
            0f, 1f, 0f, ty,
            0f, 0f, 1f, tz,
            0f, 0f, 0f, 1f
        )

        fun ortho(x: Float, y: Float, width: Float, height: Float, far: Float, near: Float) = Matrix(
            2/(width-x) , 0f            , 0f            , -(width+x)/(width-x),
            0f          , 2/(y-height)  , 0f            , -(y+height)/(y-height),
            0f          , 0f            , -2/(far-near) , -(far+near)/(far-near),
            0f          , 0f            , 0f            , 1f
        )
    }

    operator fun get(i: Int, r: Int) = elements[width * i + r]
    operator fun set(i: Int, r: Int, value: Float) {
        elements[width * i + r] = value
    }

    fun multiply(matrix: Matrix) = times(matrix)

    fun transposed(): Matrix{
        val matrix = Matrix(*elements, width = width, height = height)
        for(i in 0 until width)
            for(r in 0 until height)
                matrix[r, i] = this[i, r]
        return matrix
    }

    operator fun times(matrix: Matrix): Matrix {
        val result = Matrix(*this.elements, width = width, height = height)
        repeat(height){ i ->
            repeat(width) { r ->
                var value = 0f
                for(index in 0 until this.width)
                    value += this[i, index] * matrix[index, r]
                result[i, r] = value
            }
        }
        return result
    }

    override fun toString(): String {
        var text = "Matrix($width, $height):"
        val columnSize = elements.map { valueToString(it).length }.maxOf { it }
        repeat(height){ row ->
            text += "\n\t| "
            repeat(width){ column ->
                val value = valueToString(elements[width * row + column])
                text += value
                text += " ".repeat(1 + (columnSize - value.length))
            }
            text += "|"
        }
        return text
    }

    private fun valueToString(value: Float): String{
        return if(value == value.toInt().toFloat())
            value.toInt().toString()
        else if(value.toString().length > 5)
            value.toString().subSequence(0, 5).toString()
        else value.toString()
    }


}