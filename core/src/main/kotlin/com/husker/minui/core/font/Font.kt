package com.husker.minui.core.font

import com.husker.minui.core.MinUIObject
import com.husker.minui.core.Resources
import com.husker.minui.natives.PlatformLibrary
import com.husker.minui.natives.impl.BaseLibrary
import com.husker.minui.natives.impl.cStr
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL30
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets

open class Font private constructor(nFace: Long, val size: Int): MinUIObject() {

    companion object {
        private const val defaultSize = 16

        private val families = hashMapOf<String, LinkedHashMap<String, Font>>()
        private val fonts = arrayListOf<Font>()

        fun fromFile(file: File) = fromFile(file.absolutePath)
        fun fromFile(path: String): FontFile = FontFile(FontBackend.loadFont(path), defaultSize, File(path))
        fun fromBytes(data: ByteArray): Font = Font(FontBackend.loadFont(data), defaultSize)
        fun fromStream(stream: InputStream): Font = fromBytes(stream.readBytes())
        fun fromURL(url: URL): Font = fromBytes(url.readBytes())
        fun fromURL(url: String): Font = fromURL(URL(url))
        fun fromResource(path: String): Font = fromStream(Font::class.java.getResourceAsStream(if(path.startsWith("/")) path else "/$path")!!)

        fun registerFont(font: Font){
            val family = font.family.lowercase()
            val type = font.subfamily.lowercase()
            families.putIfAbsent(family, linkedMapOf())
            families[family]!![type] = font
        }

        val registeredFonts: Array<Font>
            get() = fonts.toTypedArray()

        val registeredFamilies: Array<String>
            get() = families.keys.toTypedArray()

        val default: Font
            get() = get("Inter")!!

        operator fun get(name: String): Font? {
            val family = name.lowercase()
            return if(family in families)
                if("regular" in families[family]!!) families[family]!!["regular"]!!
                else families[family]!!.values.first()
            else {
                val systemFonts = PlatformLibrary.instance.getFontPaths(name)
                return if(systemFonts.isNotEmpty()){
                    for(fontPath in systemFonts)
                        registerFont(fromFile(fontPath))
                    return get(family)
                }else null
            }
        }

        init {
            //registerFont(fromResource("/com/husker/minui/core/fonts/Inter-Regular.otf"))
            //registerFont(fromResource("/com/husker/minui/core/fonts/Inter-Italic.otf"))
            //registerFont(fromResource("/com/husker/minui/core/fonts/Inter-Bold.otf"))
        }
    }

    val backend = FontBackend(this, nFace)

    private var disposed = false

    val metadataSize = backend.getFontMetaCount()

    val copyright: String by lazy { getMetadata(0) }
    val family: String by lazy { getMetadata(1) }
    val subfamily: String by lazy { getMetadata(2) }
    val id: String by lazy { getMetadata(3) }
    val fullName: String by lazy { getMetadata(4) }
    val version: String by lazy { getMetadata(5) }
    val name: String by lazy { getMetadata(6) }
    val trademark: String by lazy { getMetadata(7) }
    val manufacturer: String by lazy { getMetadata(8) }
    val designer: String by lazy { getMetadata(9) }
    val description: String by lazy { getMetadata(10) }
    val vendorURL: String by lazy { getMetadata(11) }
    val designerURL: String by lazy { getMetadata(12) }

    fun resize(newSize: Int): Font = Font(backend.nFace, newSize)

    fun getMetadata(id: Int): String {
        return if(id >= metadataSize) ""
        else backend.getFontMeta(id)
    }

    fun finalize(){
        if(disposed) return
        disposed = true
        backend.dispose()
    }

    class FontFile(nFace: Long, size: Int, val file: File): Font(nFace, size)

    class FontBackend(val font: Font, val nFace: Long) {

        companion object {
            val nFtLibrary: Long = BaseLibrary.nInitFreetype()

            fun loadFont(data: ByteArray): Long {
                return checkFontError(BaseLibrary.nLoadFace(nFtLibrary, data))
            }

            fun loadFont(path: String): Long{
                if(!File(path).exists())
                    throw NullPointerException("File doesn't exist: $path")
                return checkFontError(BaseLibrary.nLoadFaceFile(nFtLibrary, path.cStr())) { path }
            }

            private fun checkFontError(status: Long, addition: () -> String? = {null}): Long{
                if(status >= 0)
                    return status
                throw FontException(when(-status.toInt()) {
                    // generic errors
                    0x01 -> "CANNOT_OPEN_RESOURCE: Cannot open resource"
                    0x02 -> "UNKNOWN_FILE_FORMAT: Unknown file format"
                    0x03 -> "INVALID_FILE_FORMAT: Broken file"
                    0x04 -> "INVALID_VERSION: Invalid FreeType version"
                    0x05 -> "LOWER_MODULE_VERSION: Module version is too low"
                    0x06 -> "INVALID_ARGUMENT: Invalid argument"
                    0x07 -> "UNIMPLEMENTED_FEATURE: Unimplemented feature"
                    0x08 -> "INVALID_TABLE: Broken table"
                    0x09 -> "INVALID_OFFSET: Broken offset within table"
                    0x0A -> "ARRAY_TOO_LARGE: Array allocation size too large"
                    0x0B -> "MISSING_MODULE: Missing module"
                    0x0C -> "MISSING_PROPERTY: Missing property"

                    // glyph/character errors
                    0x10 -> "INVALID_GLYPH_INDEX: Invalid glyph index"
                    0x11 -> "INVALID_CHARACTER_CODE: Invalid character code"
                    0x12 -> "INVALID_GLYPH_FORMAT: Unsupported glyph image format"
                    0x13 -> "CANNOT_RENDER_GLYPH: Cannot render this glyph format"
                    0x14 -> "INVALID_OUTLINE: Invalid outline"
                    0x15 -> "INVALID_COMPOSITE: Invalid composite glyph"
                    0x16 -> "TOO_MANY_HINTS: Too many hints"
                    0x17 -> "INVALID_PIXEL_SIZE: Invalid pixel size"

                    // handle errors
                    0x20 -> "INVALID_HANDLE: Invalid object handle"
                    0x21 -> "INVALID_LIBRARY_HANDLE: Invalid library handle"
                    0x22 -> "INVALID_DRIVER_HANDLE: Invalid module handle"
                    0x23 -> "INVALID_FACE_HANDLE: Invalid face handle"
                    0x24 -> "INVALID_SIZE_HANDLE: Invalid size handle"
                    0x25 -> "INVALID_SLOT_HANDLE: Invalid glyph slot handle"
                    0x26 -> "INVALID_CHARMAP_HANDLE: Invalid charmap handle"
                    0x27 -> "INVALID_CACHE_HANDLE: Invalid cache manager handle"
                    0x28 -> "INVALID_STREAM_HANDLE: Invalid stream handle"

                    // driver errors
                    0x30 -> "TOO_MANY_DRIVERS: Too many modules"
                    0x31 -> "TOO_MANY_EXTENSIONS: Too many extensions"

                    // memory errors
                    0x40 -> "OUT_OF_MEMORY: Out of memory"
                    0x41 -> "UNLISTED_OBJECT: Unlisted object"

                    // stream errors
                    0x51 -> "CANNOT_OPEN_STREAM: Cannot open stream"
                    0x52 -> "INVALID_STREAM_SEEK: Invalid stream seek"
                    0x53 -> "INVALID_STREAM_SKIP: Invalid stream skip"
                    0x54 -> "INVALID_STREAM_READ: Invalid stream read"
                    0x55 -> "INVALID_STREAM_OPERATION: Invalid stream operation"
                    0x56 -> "INVALID_FRAME_OPERATION: Invalid frame operation"
                    0x57 -> "NESTED_FRAME_ACCESS: Nested frame access"
                    0x58 -> "INVALID_FRAME_READ: Invalid frame read"

                    // raster errors
                    0x60 -> "RASTER_UNINITIALIZED: Raster uninitialized"
                    0x61 -> "RASTER_CORRUPTED: Raster corrupted"
                    0x62 -> "RASTER_OVERFLOW: Raster overflow"
                    0x63 -> "RASTER_NEGATIVE_HEIGHT: Negative height while rastering"

                    // cache errors
                    0x70 -> "TOO_MANY_CACHES: Too many registered caches"

                    // TrueType and SFNT errors
                    0x80 -> "INVALID_OPCODE: Invalid opcode"
                    0x81 -> "TOO_FEW_ARGUMENTS: Too few arguments"
                    0x82 -> "STACK_OVERFLOW: Stack overflow"
                    0x83 -> "CODE_OVERFLOW: Code overflow"
                    0x84 -> "BAD_ARGUMENT: Bad argument"
                    0x85 -> "DIVIDE_BY_ZERO: Division by zero"
                    0x86 -> "INVALID_REFERENCE: Invalid reference"
                    0x87 -> "DEBUG_OPCODE: Found debug opcode"
                    0x88 -> "ENDF_IN_EXEC_STREAM: Found ENDF opcode in execution stream"
                    0x89 -> "NESTED_DEFS: Nested DEFS"
                    0x8A -> "INVALID_CODERANGE: Invalid code range"
                    0x8B -> "EXECUTION_TOO_LONG: Execution context too long"
                    0x8C -> "TOO_MANY_FUNCTION_DEFS: Too many function definitions"
                    0x8D -> "TOO_MANY_INSTRUCTION_DEFS: Too many instruction definitions"
                    0x8E -> "TABLE_MISSING: SFNT font table missing"
                    0x8F -> "HORIZ_HEADER_MISSING: Horizontal header (hhea) table missing"
                    0x90 -> "LOCATIONS_MISSING: Locations (loca) table missing"
                    0x91 -> "NAME_TABLE_MISSING: Name table missing"
                    0x92 -> "CMAP_TABLE_MISSING: Character map (cmap) table missing"
                    0x93 -> "HMTX_TABLE_MISSING: Horizontal metrics (hmtx) table missing"
                    0x94 -> "POST_TABLE_MISSING: PostScript (post) table missing"
                    0x95 -> "INVALID_HORIZ_METRICS: Invalid horizontal metrics"
                    0x96 -> "INVALID_CHARMAP_FORMAT: Invalid character map (cmap) format"
                    0x97 -> "INVALID_PPEM: Invalid ppem value"
                    0x98 -> "INVALID_VERT_METRICS: Invalid vertical metrics"
                    0x99 -> "COULD_NOT_FIND_CONTEXT: Could not find context"
                    0x9A -> "INVALID_POST_TABLE_FORMAT: Invalid PostScript (post) table format"
                    0x9B -> "INVALID_POST_TABLE: Invalid PostScript (post) table"
                    0x9C -> "DEF_IN_GLYF_BYTECODE: Found FDEF or IDEF opcode in glyf bytecode"
                    0x9D -> "MISSING_BITMAP: Missing bitmap in strike"

                    // CFF, CID, and Type 1 errors
                    0xA0 -> "SYNTAX_ERROR: Opcode syntax error"
                    0xA1 -> "STACK_UNDERFLOW: Argument stack underflow"
                    0xA2 -> "IGNORE: Ignore"
                    0xA3 -> "NO_UNICODE_GLYPH_NAME: No Unicode glyph name found"
                    0xA4 -> "GLYPH_TOO_BIG: Glyph too big for hinting"

                    // BDF errors
                    0xB0 -> "MISSING_STARTFONT_FIELD: 'STARTFONT' field missing"
                    0xB1 -> "MISSING_FONT_FIELD: 'FONT' field missing"
                    0xB2 -> "MISSING_SIZE_FIELD: 'SIZE' field missing"
                    0xB3 -> "MISSING_FONTBOUNDINGBOX_FIELD: 'FONTBOUNDINGBOX' field missing"
                    0xB4 -> "MISSING_CHARS_FIELD: 'CHARS' field missing"
                    0xB5 -> "MISSING_STARTCHAR_FIELD: 'STARTCHAR' field missing"
                    0xB6 -> "MISSING_ENCODING_FIELD: 'ENCODING' field missing"
                    0xB7 -> "MISSING_BBX_FIELD: 'BBX' field missing"
                    0xB8 -> "BBX_TOO_BIG: 'BBX' too big"
                    0xB9 -> "CORRUPTED_FONT_HEADER: Font header corrupted or missing fields"
                    0xBA -> "CORRUPTED_FONT_GLYPHS: Font glyphs corrupted or missing fields"
                    else -> "Unknown error"
                }, -status.toInt(), addition.invoke())
            }
        }

        fun getFontMetaCount() = BaseLibrary.nGetFaceNameCount(nFace)

        fun getFontMeta(id: Int): String {
            val result = BaseLibrary.nGetFaceName(nFace, id)
            val bytes = result[0] as ByteArray
            val encoding = result[1] as IntArray

            // - Windows (BMP)
            // - Standard Unicode (2.0)
            // or
            // - Standard Unicode
            return if(
                (encoding[0] == 3 && encoding[1] == 1) ||
                (encoding[0] == 0 && encoding[1] == 3))
                bytes.toString(StandardCharsets.UTF_16BE)
            else
                bytes.toString(StandardCharsets.UTF_8)
        }

        fun getGlyphTexture(char: Char): Int{
            BaseLibrary.nSetFaceSize(nFace, font.size)
            BaseLibrary.nFtLoadChar(nFace, char.code)
            val data = BaseLibrary.nFtGetGlyphData(nFace)
            val width = BaseLibrary.nFtGetGlyphWidth(nFace).toInt()
            val height = BaseLibrary.nFtGetGlyphHeight(nFace).toInt()

            var texId = 0
            Resources.invokeSync{
                texId = glGenTextures()

                glBindTexture(GL_TEXTURE_2D, texId)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
                glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, width, height, 0, GL_ALPHA, GL_UNSIGNED_BYTE, data)
            }
            return texId
        }

        fun dispose(){
            checkFontError(BaseLibrary.nDoneFace(nFace))
        }


    }

}