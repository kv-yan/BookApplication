package am.innline.book.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomItem(
    val name: String,
    val destination: Destination,
    val icon: ImageVector,
)