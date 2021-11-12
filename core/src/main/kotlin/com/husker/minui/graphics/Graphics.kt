package com.husker.minui.graphics

import com.husker.minui.geometry.Rectangle
import org.lwjgl.opengl.GL11.*


class Graphics {

    var renderingWidth: Double = 0.0
    var renderingHeight: Double = 0.0

    private var _paint: Paint = Color.Black
    var paint: Paint
        get() = _paint
        set(value) {
            _paint = value
        }

    private var _width = 1.0
    var width: Double
        get() = _width
        set(value) {
            _width = value
            glLineWidth(value.toFloat())
        }

    private var _translateX = 0.0
    var translateX: Double
        get() = _translateX
        set(value) {
            glTranslatef((value - _translateX).toFloat(), 0.0f, 0.0f)
            _translateX = value
        }

    private var _translateY = 0.0
    var translateY: Double
        get() = _translateY
        set(value) {
            glTranslatef(0.0f, (value - _translateY).toFloat(), 0.0f)
            _translateY = value
        }

    private var _clip: Rectangle? = null
    var clip: Rectangle?
        get() = _clip
        set(value) {
            if(value != null)
                glEnable(GL_SCISSOR_TEST)
            else
                glDisable(GL_SCISSOR_TEST)
            _clip = value
        }


    private fun prepareRendering(){
        paint.apply()
        _clip?.let { glScissor(it.x.toInt(), (renderingHeight - it.y - it.height).toInt(), it.width.toInt(), (it.height).toInt()) }
    }

    fun fillRect(x: Double, y: Double, width: Double, height: Double){
        prepareRendering()
        glBegin(GL_QUADS)
        glVertex2d(x, y)
        glVertex2d(x + width, y)
        glVertex2d(x + width, y + height)
        glVertex2d(x, y + height)
        glEnd()
    }

    fun drawRect(x: Double, y: Double, width: Double, height: Double){
        prepareRendering()
        glBegin(GL_LINE_LOOP)
        glVertex2d(x, y)
        glVertex2d(x + width, y)
        glVertex2d(x + width, y + height)
        glVertex2d(x, y + height)
        glEnd()
    }

    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double){
        drawImage(image, x, y, width, height, Color.White)
    }

    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double, color: Color){
        if(image.textId != null)
            drawImage(image.textId!!, x, y, width, height, color)
    }

    fun drawImage(texId: Int, x: Double, y: Double, width: Double, height: Double){
        drawImage(texId, x, y, width, height, Color.White)
    }

    fun drawImage(texId: Int, x: Double, y: Double, width: Double, height: Double, color: Color){
        prepareRendering()
        color.apply()

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texId)
        glBegin(GL_QUADS)

        glTexCoord2f(0.0f, 0.0f)
        glVertex2d(x, y)
        glTexCoord2f(1.0f, 0.0f)
        glVertex2d(x + width, y)
        glTexCoord2f(1.0f, 1.0f)
        glVertex2d(x + width, y + height)
        glTexCoord2f(0.0f, 1.0f)
        glVertex2d(x, y + height)
        glEnd()
    }
}