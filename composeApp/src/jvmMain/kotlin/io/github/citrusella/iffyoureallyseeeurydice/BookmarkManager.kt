package io.github.citrusella.iffyoureallyseeeurydice

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.bookmarkData
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.fromBookmarkData
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write

object BookmarkManager {
    private val bookmarkFile = FileKit.filesDir / "bookmark.bin"

    suspend fun save(file: PlatformFile) {
        try {
            val bookmark = file.bookmarkData()
            bookmarkFile.write(bookmark.bytes)
        } catch (e: Exception) {
            // Handle exceptions, e.g., log the error
            println("Error saving bookmark: ${e.message}")
        }
    }

    suspend fun load(): PlatformFile? {
        if (!bookmarkFile.exists()) return null

        return try {
            val bytes = bookmarkFile.readBytes()
            val file = PlatformFile.fromBookmarkData(bytes)

            // Best practice: verify the file still exists
            if (file.exists()) {
                file
            } else {
                // The file was moved or deleted, so clean up the stale bookmark
                clear()
                null
            }
        } catch (e: Exception) {
            // Bookmark is invalid or corrupted, clean it up
            clear()
            null
        }
    }

    suspend fun clear() {
        try {
            if (bookmarkFile.exists()) {
                bookmarkFile.delete()
            }
        } catch (e: Exception) {
            println("Error clearing bookmark: ${e.message}")
        }
    }
}