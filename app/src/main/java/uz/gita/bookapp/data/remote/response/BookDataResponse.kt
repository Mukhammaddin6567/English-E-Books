package uz.gita.bookapp.data.remote.response

import uz.gita.bookapp.data.local.entity.BookEntity
import uz.gita.bookapp.data.model.BookData

data class BookDataResponse(
    val id: Int? = null,
    val image: String? = null,
    val name: String? = null,
    val author: String? = null,
    val details: String? = null,
    val reference: String? = null,

    ) {
    fun toBookData(): BookData =
        BookData(
            id = id ?: 0,
            image = image ?: "",
            name = name ?: "",
            author = author ?: "",
            details = details ?: "",
            reference = reference ?: ""
    )

    fun toBookEntity(): BookEntity =
        BookEntity(
            id = id ?: 0,
            image = image ?: "",
            name = name ?: "",
            author = author ?: "",
            details = details ?: "",
            reference = reference ?: ""
        )
}
