package am.innline.book.search.data.model

data class VolumeInfoDto(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinksDto?
)