package pract2

import kotlinx.coroutines.*
import java.io.File
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile

fun main() {
    runBlocking {
        try {
            withTimeout(2000) {
                val files = findJsonFiles()
                val map = calculateHashForFiles(files)
                val duplicates = findDuplicates(map)
                if (duplicates.isEmpty()){
                    println("No duplicates were found")
                } else {
                    duplicates.map { group ->
                        println(group.joinToString(" ") { it.name })
                    }
                }
            }
        } catch (e: NoSuchFileException){
            println(e.message)
//        } catch (e: NotDirectoryException){
//            println(e.message)
        } catch (e: TimeoutCancellationException){
            println("Search aborted due to time out")
        }
    }
}


// function that I got from https://ssojet.com/hashing/sha-256-in-kotlin/#handling-different-input-types
// encodes any file into SHA-256 string
suspend fun sha256File(file: File): String = withContext(Dispatchers.IO) {
    val digest = MessageDigest.getInstance("SHA-256")
    file.inputStream().use { inputStream ->
        val buffer = ByteArray(1024) // Read in 1KB chunks
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            digest.update(buffer, 0, bytesRead) // Update digest with chunk
        }
    }
    val hashBytes = digest.digest()
    hashBytes.fold("") { str, byte -> str + "%02x".format(byte) }
}

fun findJsonFiles(): List<File> {
    val directoryPath = "C:\\Users\\Aorus\\Desktop\\json_files"
    val directory = File(directoryPath)

    if (!directory.exists()) {
        throw NoSuchFileException("No such directory")
    }

//    if (!directory.isDirectory){
//        throw NotDirectoryException("This is not directory")
//    }

    val files = Files.walk(Path(directoryPath))
        .filter{ file ->
            file.isRegularFile() && file.extension.lowercase() == "json"
        }
        .map{ file ->
            file.toFile()
        }
        .toList()
    return files
}

suspend fun calculateHashForFiles(files: List<File>): Map<File, String> =
    coroutineScope {
//        if (files.isNullOrEmpty()) {
//            println("No files found")
//            return mutableMapOf()
//        }
        val filesAndHashesMap = files.map { file ->
            async {
                file to sha256File(file)
            }
        }
        filesAndHashesMap.awaitAll().toMap()
    }

/*
    groupBy logic:
        Without valueTransform it makes Map<String, List<Map<String, File>>> out of hash and all files that have the same hash.
        But with valueTransform it drops out String (hash) from inner map in List and makes the result more readable (Map<String, List<File>>)
    filter logic:
        leave only the maps with a list of more than 1 files which basically means there are duplicates
    values + toList logic:
        leave only lists with files and wrap it in one big list of lists
 */
fun findDuplicates(filesAndHashesMap: Map<File, String>): List<List<File>>{
    val groupsOfDuplicates = filesAndHashesMap.entries
        .groupBy(keySelector = { it.value }, valueTransform = { it.key })
        .filter { it.value.size > 1 }
        .values
        .toList()
    return groupsOfDuplicates
}