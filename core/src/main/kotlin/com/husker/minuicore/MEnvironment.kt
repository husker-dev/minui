package com.husker.minuicore

import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.deleteIfExists

class MLEnvironment {
    companion object {
        private val tempDir = File("${System.getProperty("java.io.tmpdir")}/minui_cache/${MLCore.version}")
        private const val fileLockName = "dir.lock"

        val folder = getCompatibleFolder()

        private fun getCompatibleFolder(): File {
            val unlocked = getUnlockedFolders()

            if (unlocked.isNotEmpty()) {
                val file = unlocked[0]

                unlocked.forEach {  // Remove other folders
                    if (it != file) {
                        Files.walk(Paths.get(it.absolutePath))
                            .sorted(Comparator.reverseOrder())
                            .forEach(Path::deleteIfExists)
                    }
                }
                lockFolder(file)
                return file
            }

            val file = File(tempDir, System.nanoTime().toString())
            lockFolder(file)
            return file
        }

        private fun getUnlockedFolders(): ArrayList<File> {
            val unlockedFiles = arrayListOf<File>()

            tempDir.mkdirs()
            for (file in tempDir.listFiles()!!) {
                if (fileLockName in file.list()!!) {
                    val fileLock = File(file, fileLockName)
                    if (fileLock.renameTo(fileLock))
                        unlockedFiles.add(file)
                } else
                    unlockedFiles.add(file)
            }
            return unlockedFiles
        }

        private fun lockFolder(file: File) {
            file.mkdirs()
            val fileLock = File(file, fileLockName)
            if (!fileLock.exists())
                fileLock.createNewFile()

            val channel = RandomAccessFile(fileLock, "rw").channel
            channel.lock()
        }
    }
}