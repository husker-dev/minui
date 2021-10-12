package com.husker.minui.natives

import com.husker.minui.core.MinUI
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.*
import kotlin.io.path.deleteIfExists


class LibraryUtils {

    companion object {

        @JvmStatic
        var forceLoad = false
        private val tmpDir = "${System.getProperty("java.io.tmpdir")}/minui_cache/${MinUI.version}"
        private var currentDir = ""

        init{
            val directoriesStates = hashMapOf<File, Boolean>()

            File(tmpDir).mkdirs()
            File(tmpDir).listFiles()!!.forEach {
                val children = it.listFiles()!!
                directoriesStates[it] = children.isEmpty() || (children[0].canRead() && children[0].canWrite())
            }

            var found = false
            directoriesStates.entries.forEach {
                if(it.value){
                    if(found){
                        try {
                            Files.walk(Paths.get(it.key.path))
                                .sorted(Comparator.reverseOrder())
                                .forEach(Path::deleteIfExists)
                        }catch (e: Exception){ }
                    }else{
                        found = true
                        currentDir = it.key.path
                    }
                }
            }
            if(!found)
                currentDir = "$tmpDir/${System.nanoTime()}"
        }

        @JvmStatic
        fun loadResourceLibrary(name: String){
            val file = File("$currentDir/$name")
            if(file.exists() && !forceLoad) {
                System.load(file.absolutePath)
                return
            }
            file.parentFile.mkdirs()

            val inp: InputStream = this::class.java.getResourceAsStream(name)!!

            val fos = FileOutputStream(file)

            val buffer = ByteArray(1024)
            var read: Int
            while (inp.read(buffer).also { read = it } != -1)
                fos.write(buffer, 0, read)

            fos.close()
            inp.close()

            System.load(file.absolutePath)
        }


    }
}