package com.husker.minui.components

import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Graphics
import com.husker.minui.graphics.Image

class ImageView(): Component() {

    enum class FitType{
        Fit,
        Fill,
        Resize
    }

    var image: Image? = null
    var fitType = FitType.Resize
    var color = Color.White

    constructor(image: Image, color: Color): this(){
        this.image = image
        this.color = color
    }

    constructor(image: Image): this(){
        this.image = image
    }

    init {
        preferredWidth = 200.0
        preferredHeight = 200.0
    }

    override fun draw(gr: Graphics) {
        if(isZeroSize() || image == null)
            return
        if(fitType == FitType.Resize)
            gr.drawImage(image!!, 0.0, 0.0, width, height, color)

    }
}