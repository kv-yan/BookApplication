package am.innline.book.details.presentation.utils

import androidx.core.net.toUri
import java.io.File

fun isUri(input: String?): Boolean {
    if (input.isNullOrEmpty()) return false
    return try {
        input.toUri().scheme == "content" || File(input).exists()
    } catch (e: Exception) {
        false
    }
}