package uz.gita.bookapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.bookapp.data.model.BookData

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String,
    val name: String,
    val author: String,
    val details: String,
    val reference: String,
    val currentPage: Int = 0,
    val pagesCount: Int = 0
) {
    fun toBookData(): BookData =
        BookData(
            id = id,
            image = image,
            name = name,
            author = author,
            details = details,
            reference = reference,
            currentPage = currentPage,
            pagesCount = pagesCount
        )
}