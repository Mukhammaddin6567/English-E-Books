package uz.gita.bookapp.data.model

data class BookData(
    val id: Int,
    val image: String,
    val name: String,
    val author: String,
    val details: String,
    val reference: String,
    var currentPage: Int = 0,
    var pagesCount: Int = 0
)