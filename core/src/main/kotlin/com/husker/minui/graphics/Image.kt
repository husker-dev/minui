package com.husker.minui.graphics

import com.husker.minui.core.MinUIEnvironment
import com.husker.minui.core.MinUIObject
import com.husker.minui.core.Resources
import com.husker.minui.core.utils.BufferUtils
import com.husker.minui.graphics.ImageEncoding.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage.*
import org.lwjgl.stb.STBImageResize.*
import org.lwjgl.stb.STBImageWrite.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.ByteBuffer

/**  Encoding Types
 *   ##### Enum values:
 *
 * - [PNG][ImageEncoding.PNG] - 4 channels
 * - [JPEG][ImageEncoding.JPEG] - 3 channels, less size
 * - [BMP][ImageEncoding.BMP] - 4 channels
 */
enum class ImageEncoding {
    PNG,
    JPEG,
    BMP,
    TGA
}

/**  Resize Types
 *   ##### Enum values:
 *
 * - [Nearest][ResizeType.Nearest] - "Nearest" texture filtering in OpenGL
 * - [Linear][ResizeType.Linear] - "Linear" texture filtering in OpenGL
 * - [Box][ResizeType.Box] - A trapezoid w/1-pixel wide ramps, same result as box for integer scale ratios.
 * - [Triangle][ResizeType.Triangle] - On upsampling, produces same results as bilinear texture filtering.
 * - [CubicBSpline][ResizeType.CubicBSpline] - The cubic b-spline (aka Mitchell-Netrevalli with B=1,C=0), gaussian-esque.
 * - [CatmullRom][ResizeType.CatmullRom] - An interpolating cubic spline.
 * - [Mitchell][ResizeType.Mitchell] - Mitchell-Netrevalli filter with B=1/3, C=1/3.
 */
enum class ResizeType(var value: Int) {
    Nearest(-2),
    Linear(-1),
    Box(1),
    Triangle(2),
    CubicBSpline(3),
    CatmullRom(4),
    Mitchell(5),
}

open class Image: MinUIObject {

    companion object{
        private var count = 0

        /**
         * Creates empty image with specified size
         *
         * @param width image width
         * @param height image height
         * @return Empty image object
         */
        fun create(width: Int, height: Int): Image = Image(width, height)

        /**
         * Downloads image from specified URL string
         *
         * @param url image URL string
         * @return Downloaded image object
         */
        fun fromURL(url: String): Image = Image(URL(url))

        /**
         * Downloads image from specified URL
         *
         * @param url image URL
         * @return Downloaded image object
         */
        fun fromURL(url: URL): Image = Image(url)

        /**
         * Loads image from specified file
         *
         * @param file image file
         * @return Image object
         */
        fun fromFile(file: File): Image = Image(file.absolutePath)

        /**
         * Loads image from specified file path
         *
         * @param path image file path
         * @return Image object from file
         */
        fun fromFile(path: String): Image = Image(path)

        /**
         * Loads image from specified resource file path (inside jar file)
         *
         * The path can start **with** or **without** "/"
         *
         * @param path resources file path to image
         * @return Image object from resources
         */
        fun fromResource(path: String): Image = if(path.startsWith("/")) Image(path) else Image("/$path")

        /**
         * Loads image from [InputStream][InputStream]
         *
         * @param inputStream stream of image file (not bitmap)
         * @return Image object
         */
        fun fromInputStream(inputStream: InputStream): Image = Image(inputStream)

        /**
         * Loads image from file bytes **(not bitmap)**
         *
         * @param bytes image file bytes
         * @return Image object
         */
        fun fromBytes(bytes: ByteArray): Image = Image(bytes)

        /**
         * Loads image from file [ByteBuffer][ByteBuffer] **(not bitmap)**
         *
         * @param buffer image file byte buffer
         * @return Image object
         */
        fun fromByteBuffer(buffer: ByteBuffer): Image = Image(buffer)

        /**
         * Loads image from image [ByteBuffer][ByteBuffer] **(bitmap)**
         *
         * @param buffer image byte buffer
         * @param width image width
         * @param height image height
         * @param components components count (RGB = 3, RGBA = 4)
         * @return Image object
         */
        fun fromByteBuffer(buffer: ByteBuffer, width: Int, height: Int, components: Int): Image = Image(buffer, width, height, components)

        /**
         * Loads image from image bytes **(bitmap)**
         *
         * @param bytes image byte array
         * @param width image width
         * @param height image height
         * @param components components count (RGB = 3, RGBA = 4)
         * @return Image object
         */
        fun fromBytes(bytes: ByteArray, width: Int, height: Int, components: Int): Image = Image(bytes, width, height, components)
    }

    private val id = count++

    private var changed = false

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
    var data: ByteBuffer
        get() {
            if(changed) {
                changed = false
                _data = Resources.readTextureBytes(textId!!, width, height)
            }
            return _data
        }
        set(value) {
            Resources.writeTextureBytes(textId!!, width, height, value)
            changed = true
        }

    private var _linearFiltering = true
    var linearFiltering: Boolean
        get() = _linearFiltering
        set(value) {
            _linearFiltering = value
            Resources.invokeSync {
                glBindTexture(GL_TEXTURE_2D, textId!!)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if (value) GL_LINEAR else GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, if (value) GL_LINEAR else GL_NEAREST)
            }
        }

    private constructor(path: String): super(){
        if(path.startsWith("/"))
            loadFromByteArray(Image::class.java.getResourceAsStream(path)!!.readBytes())
        else
            loadFromFile(path)
        configureImage()
    }

    private constructor(inputStream: InputStream): this(inputStream.readBytes())

    // Formats as PNG, JPEG, etc.
    private constructor(bytes: ByteArray): super(){
        loadFromByteArray(bytes)
        configureImage()
    }

    // Formats as PNG, JPEG, etc.
    private constructor(buffer: ByteBuffer): super(){
        if(buffer.isDirect)
            loadFromDirectByteBuffer(buffer)
        else
            loadFromByteArray(buffer.array())
        configureImage()
    }

    // Pure bitmap
    private constructor(buffer: ByteBuffer, width: Int, height: Int, components: Int): super(){
        applyData(buffer, width, height, components)
        configureImage()
    }

    // Pure bitmap
    private constructor(bytes: ByteArray, width: Int, height: Int, components: Int): super(){
        val directBuffer = ByteBuffer.allocateDirect(bytes.size)
        directBuffer.put(bytes)
        BufferUtils.flipBuffer(directBuffer)

        applyData(directBuffer, width, height, components)
        configureImage()
    }

    private constructor(url: URL): this(url.openStream())

    private constructor(width: Int, height: Int){
        this._width = width
        this._height = height
        this._components = 4

        val buffer = ByteBuffer.allocateDirect(width * height * _components)
        buffer.put(ByteArray(width * height * _components))
        BufferUtils.flipBuffer(buffer)

        this._data = buffer

        var id = 0
        Resources.invokeSync{ id = createEmptyTexture() }
        _textId = id
        configureImage()
    }

    private fun configureImage(){
        linearFiltering = linearFiltering
    }

    private fun applyData(loadedData: ByteBuffer, width: Int, height: Int, components: Int){
        this._width = width
        this._height = height
        this._components = components
        this._data = loadedData

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
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, MemoryUtil.NULL)

        return id
    }

    private fun loadFromFile(path: String){
        stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val components = stack.mallocInt(1)

            val data = stbi_load(path, w, h, components, 4)!!
            applyData(data, w[0], h[0], 4)
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

            val data = stbi_load_from_memory(buffer, w, h, stack.mallocInt(1), 4)!!
            applyData(data, w[0], h[0], 4)
        }
    }

    /**
     * Creates new image instance with specified size
     *
     * @param newWidth new image width
     * @param newHeight new image height
     * @param type resizing type
     * @return Resized image instance
     */
    fun resize(newWidth: Int, newHeight: Int, type: ResizeType = ResizeType.CubicBSpline): Image{
        val buffer = ByteBuffer.allocateDirect(newWidth * newHeight * 4)
        if(type.value < 0){
            Resources.resizeTexture(textId!!, newWidth, newHeight, buffer, linear= type == ResizeType.Linear)
        }else {
            stbir_resize(
                data, width, height, 0,
                buffer, newWidth, newHeight, 0,
                STBIR_TYPE_UINT8, 4, 3, 0,
                STBIR_EDGE_CLAMP,
                STBIR_EDGE_CLAMP,
                type.value,
                type.value,
                STBIR_COLORSPACE_SRGB
            )
        }
        return fromByteBuffer(buffer, newWidth, newHeight, components)
    }

    /**
     * Saves image to a specified file
     *
     * @param file destination file
     * @param encoding bitmap encoding
     * @param quality defines compression quality. Used only for [JPEG][ImageEncoding.JPEG] encoding
     */
    fun save(file: File, encoding: ImageEncoding = PNG, quality: Int = 90) = save(file.absolutePath, encoding, quality)

    /**
     * Saves image to a specified file path
     *
     * @param path destination file path
     * @param encoding bitmap encoding
     * @param quality defines compression quality. Used only for [JPEG][ImageEncoding.JPEG] encoding
     */
    fun save(
        path: String,
        encoding: ImageEncoding = PNG,
        quality: Int = 90
    ){
        when(encoding){
            PNG -> stbi_write_png(path, width, height, components, data, width * components)
            JPEG -> stbi_write_jpg(path, width, height, components, data, quality)
            BMP -> stbi_write_bmp(path, width, height, components, data)
            TGA -> stbi_write_tga(path, width, height, components, data)
        }
    }

    fun cacheFile(): File {
        val file = File(MinUIEnvironment.file, "cached_images/${id}.png")
        file.parentFile.mkdirs()
        save(file, PNG)
        return file
    }

    override fun toString(): String {
        return "Image(width=$_width, height=$_height, components=$_components, textId=$_textId)"
    }
}