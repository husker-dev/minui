package com.husker.minui.graphics

import com.husker.minui.core.MinUIObject
import com.husker.minui.core.Resources
import com.husker.minui.core.utils.BufferUtils
import org.lwjgl.BufferUtils.createByteBuffer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage.stbi_load
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack.stackPush
import java.io.File
import java.io.InputStream
import java.lang.reflect.Method
import java.net.URL
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.channels.Channels


open class Image: MinUIObject {

    companion object{
        fun create(width: Int, height: Int): Image = Image(width, height)
        fun fromURL(url: String): Image = Image(URL(url))
        fun fromURL(url: URL): Image = Image(url)
        fun fromFile(file: File): Image = Image(file.absolutePath)
        fun fromFile(path: String): Image = Image(path)
        fun fromResourceFile(path: String): Image = Image(path)
        fun fromInputStream(inputStream: InputStream): Image = Image(inputStream)
    }

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

    private lateinit var _data: ByteBuffer
    val data: ByteBuffer
        get() = _data

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

    private fun applyData(loadedData: ByteBuffer, width: Int, height: Int, components: Int){
        this._data = loadedData
        this._width = width
        this._height = height
        this._components = components

        Resources.invokeSync{
            val id = createEmptyTexture()

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, loadedData)
            glFlush()

            _textId = id
        }
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
        }
    }

    private fun loadFromInputStream(inputStream: InputStream){
        stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val components = stack.mallocInt(1)

            var buffer: ByteBuffer
            inputStream.use { source ->
                Channels.newChannel(source).use { rbc ->
                    buffer = createByteBuffer(1024 * 8)
                    while (rbc.read(buffer) != -1) {
                        if (buffer.remaining() == 0)
                            buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2) // 50%
                    }
                }
            }
            BufferUtils.flipBuffer(buffer)

            val data = stbi_load_from_memory(buffer, w, h, components, 4)!!
            applyData(data, w[0], h[0], components[0])
        }
    }

    constructor(path: String): super(){
       if(path.startsWith("/"))
           loadFromInputStream(Image::class.java.getResourceAsStream(path)!!)
       else
           loadFromFile(path)
    }

    constructor(inputStream: InputStream): super(){
        loadFromInputStream(inputStream)
    }

    constructor(url: URL): this(url.openStream())

    constructor(width: Int, height: Int){
        this._data = ByteBuffer.allocateDirect(4 * width * height)
        this._width = width
        this._height = height
        this._components = 4
        Resources.invokeSync{ _textId = createEmptyTexture() }
    }

    private fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
        val newBuffer = createByteBuffer(newCapacity)
        BufferUtils.flipBuffer(buffer)
        newBuffer.put(buffer)
        return newBuffer
    }
}