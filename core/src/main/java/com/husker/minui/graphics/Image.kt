package com.husker.minui.graphics

import com.husker.minui.core.MinUIObject
import com.husker.minui.core.Resources
import com.husker.minui.core.utils.BufferUtils
import com.husker.minui.graphics.ImageEncoding.*
import com.husker.minui.natives.LibraryUtils
import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage.*
import org.lwjgl.stb.STBImageWrite.*
import org.lwjgl.system.MemoryStack.stackPush
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.ByteBuffer

enum class ImageEncoding {
    PNG,
    JPEG,
    BMP,
    TGA
}

open class Image: MinUIObject {

    companion object{
        private var count = 0

        fun create(width: Int, height: Int): Image = Image(width, height)
        fun fromURL(url: String): Image = Image(URL(url))
        fun fromURL(url: URL): Image = Image(url)
        fun fromFile(file: File): Image = Image(file.absolutePath)
        fun fromFile(path: String): Image = Image(path)
        fun fromResource(path: String): Image = if(path.startsWith("/")) Image(path) else Image("/$path")
        fun fromInputStream(inputStream: InputStream): Image = Image(inputStream)
        fun fromBytes(bytes: ByteArray): Image = Image(bytes)
        fun fromByteBuffer(buffer: ByteBuffer): Image = Image(buffer)
    }

    private val id = count++

    private var _width = 0
    val width: Int
        get() = _width

    private var _height = 0
    val height: Int
        get() = _height

    private var _components = 0
    val components: Int
        get() = _components

    private var _textId: Int? = null
    val textId: Int?
        get() = _textId

    var data: ByteBuffer
        get() = Resources.readTextureBytes(textId!!, width, height)
        set(value) = Resources.writeTextureBytes(textId!!, width, height, value)

    private var _linearFiltering = true
    var linearFiltering: Boolean
        get() = _linearFiltering
        set(value) {
            _linearFiltering = value
            // TODO: implement linear filtering
            /*
            glBindTexture(GL_TEXTURE_2D, textId)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if(value) GL_LINEAR else GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, if(value) GL_LINEAR else GL_NEAREST)

             */
        }

    constructor(path: String): super(){
       if(path.startsWith("/"))
           loadFromByteArray(Image::class.java.getResourceAsStream(path)!!.readBytes())
       else
           loadFromFile(path)
    }

    constructor(inputStream: InputStream): this(inputStream.readBytes())

    constructor(bytes: ByteArray): super(){
        loadFromByteArray(bytes)
    }

    constructor(buffer: ByteBuffer): super(){
        if(buffer.isDirect)
            loadFromDirectByteBuffer(buffer)
        else
            loadFromByteArray(buffer.array())
    }

    constructor(url: URL): this(url.openStream())

    constructor(width: Int, height: Int){
        this._width = width
        this._height = height
        this._components = 4

        Resources.invokeSync{ _textId = createEmptyTexture() }
    }

    private fun applyData(loadedData: ByteBuffer, width: Int, height: Int, components: Int){
        this._width = width
        this._height = height
        this._components = components

        var id = 0
        Resources.invokeSync{ id = createEmptyTexture() }
        Resources.writeTextureBytes(id, width, height, loadedData)

        _textId = id
    }

    private fun createEmptyTexture(): Int{
        val id = glGenTextures()

        glBindTexture(GL_TEXTURE_2D, id)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glGenerateMipmap(GL_TEXTURE_2D)
        return id
    }

    private fun loadFromFile(path: String){
        stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val components = stack.mallocInt(1)
            val data = stbi_load(path, w, h, components, 4)!!
            applyData(data, w[0], h[0], components[0])
            stbi_image_free(data)
        }
    }

    private fun loadFromByteArray(array: ByteArray){
        val directBuffer = ByteBuffer.allocateDirect(array.size)
        directBuffer.put(array)
        BufferUtils.flipBuffer(directBuffer)

        loadFromDirectByteBuffer(directBuffer)
    }

    private fun loadFromDirectByteBuffer(buffer: ByteBuffer){
        stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val components = stack.mallocInt(1)
            val data = stbi_load_from_memory(buffer, w, h, components, 4)!!
            applyData(data, w[0], h[0], components[0])
            stbi_image_free(data)
        }
    }

    fun save(encoding: ImageEncoding, file: File, quality: Int = 90) = save(encoding, file.absolutePath, quality)

    fun save(encoding: ImageEncoding, path: String, quality: Int = 90){
        when(encoding){
            PNG -> stbi_write_png(path, width, height, 4, data, width * 4)
            JPEG -> stbi_write_jpg(path, width, height, 4, data, quality)
            BMP -> stbi_write_bmp(path, width, height, 4, data)
            TGA -> stbi_write_tga(path, width, height, 4, data)
        }
    }

    fun cacheFile(): File {
        val file = File(LibraryUtils.currentTmpDir, "cached_images/${id}.png")
        file.parentFile.mkdirs()
        save(PNG, file)
        return file
    }

    override fun toString(): String {
        return "Image(width=$_width, height=$_height, components=$_components, textId=$_textId)"
    }


}