package uz.gita.bookapp.data.repository

import android.content.Context
import uz.gita.bookapp.data.local.entity.BookEntity
import uz.gita.bookapp.data.remote.response.BookDataResponse

interface BookRepository {

    suspend fun loadAllBooks(
        success: (List<BookDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun loadBookByReference(
        context: Context,
        bookReference: String,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    )

    suspend fun saveAllBooks(books: List<BookEntity>)

    suspend fun getAllBooks(): List<BookEntity>

    suspend fun getAllBooksByQuery(query: String): List<BookEntity>

    suspend fun getBookData(bookId: Int): BookEntity

    suspend fun checkForAvailableBooks(): Boolean

    suspend fun saveCurrentBookPage(bookId: Int, currentPage: Int, pageCount: Int)

    suspend fun getUiModeStatus():Boolean

    suspend fun setUiModeStatus():Boolean

}