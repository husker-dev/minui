package com.husker.minui.components

import com.husker.minui.graphics.Color
import com.husker.minui.graphics.Graphics
import com.husker.minui.graphics.Image

enum class FitType{
    Fill,
    Fit,
    Cover
}

class ImageView(
    var image: Image? = null,
    var fitType: FitType = FitType.Fit,
    var color: Color = Color.White
): Component() {

    init {
        preferredWidth = 200.0
        preferredHeight = 200.0
    }

    override fun draw(gr: Graphics) {
        if(isZeroSize() || image == null)
            return
        when(fitType){
            FitType.Fill -> {
                gr.drawImage(image!!, 0.0, 0.0, width, height, color)
            }
            FitType.Cover -> {
                val imageHeight: Double
                val imageWidth: Double
                if(height > width){
                    imageWidth = height / image!!.height * image!!.width
                    imageHeight = height
                }else{
                    imageWidth = width
                    imageHeight = width / image!!.width * image!!.height
                }
                gr.drawImage(image!!, (width - imageWidth) / 2.0, (height - imageHeight) / 2.0, imageWidth, imageHeight, color)
            }
            FitType.Fit -> {
                val imageHeight: Double
                val imageWidth: Double
                if(height > width){
                    imageWidth = width
                    imageHeight = width / image!!.width * image!!.height
                    gr.drawImage(image!!, (width - imageWidth) / 2.0, (height - imageHeight) / 2.0, imageWidth, imageHeight, color)
                }else{
                    imageWidth = height / image!!.height * image!!.width
                    imageHeight = height
                }
                gr.drawImage(image!!, (width - imageWidth) / 2.0, (height - imageHeight) / 2.0, imageWidth, imageHeight, color)
            }
        }

    }
}