package com.husker.minui.core

import com.husker.minui.natives.PlatformLibrary
import com.husker.minui.natives.impl.BaseLibrary
import java.io.File
import java.io.InputStream
import java.net.URL

open class Font private constructor(private val nFace: Long, val size: Int): MinUIObject() {

    companion object {
        private const val defaultSize = 16

        private val families = hashMapOf<String, LinkedHashMap<String, Font>>()
        private val fonts = arrayListOf<Font>()

        fun fromFile(file: File) = fromFile(file.absolutePath)
        fun fromFile(path: String): FontFile = FontFile(BaseLibrary.loadFont(path), defaultSize, File(path))
        fun fromBytes(data: ByteArray): Font = Font(BaseLibrary.loadFont(data), defaultSize)
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
            registerFont(fromResource("/com/husker/minui/core/fonts/Inter-Regular.otf"))
            registerFont(fromResource("/com/husker/minui/core/fonts/Inter-Italic.otf"))
            registerFont(fromResource("/com/husker/minui/core/fonts/Inter-Bold.otf"))
        }
    }

    private var disposed = false

    val metadataSize = BaseLibrary.getFontMetaCount(nFace)

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

    fun resize(newSize: Int): Font = Font(nFace, newSize)

    fun getMetadata(id: Int): String {
        return if(id >= metadataSize) ""
        else BaseLibrary.getFontMeta(nFace, id)
    }

    fun finalize(){
        if(disposed) return
        disposed = true
        BaseLibrary.doneFont(nFace)
    }

    class FontFile(nFace: Long, size: Int, val file: File): Font(nFace, size)
}