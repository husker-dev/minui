package com.husker.minui.natives.impl.win

import com.husker.minui.core.Font
import com.husker.minui.natives.impl.win.WinRegistry.Companion.HKEY_CURRENT_USER
import com.husker.minui.natives.impl.win.WinRegistry.Companion.HKEY_LOCAL_MACHINE
import java.io.File
import java.util.ArrayList

class WinFonts { companion object{

    private val cachedFonts = hashMapOf<String, ArrayList<String>>()
    private val cachedFontsNames = hashMapOf<String, String>() // calibri -> Calibri

    init{
        val fontsDir = File(File(System.getenv("ProgramFiles")).parentFile,"\\Windows\\Fonts").absoluteFile
        val fonts = WinRegistry.getFolderValuesMap(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Fonts").values
            .map { "$fontsDir\\$it" } + WinRegistry.getFolderValuesMap(HKEY_CURRENT_USER, "Software\\Microsoft\\Windows NT\\CurrentVersion\\Fonts").values

        fonts.filter { it.endsWith(".ttf") || it.endsWith(".otf") }
            .forEach {
                val font = Font.fromFile(it)
                val name = font.family
                cachedFontsNames[name.lowercase()] = name

                cachedFonts.putIfAbsent(name, arrayListOf())
                cachedFonts[name]!!.add(font.file.absolutePath)
                font.finalize()
            }
    }

    fun getFontPaths(family: String): List<String> {
        if(family.lowercase() !in cachedFontsNames)
            throw NullPointerException("Font not found: $family")
        return cachedFonts[cachedFontsNames[family.lowercase()]]!!
    }
}}