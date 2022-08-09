package uz.gita.bookapp.domain.usecase.books

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import uz.gita.bookapp.data.model.BookData

interface BooksUC {

    fun loadBooksList(scope: CoroutineScope): Flow<Result<List<BookData>>>

    fun loadBookByReference(context: Context, bookReference: String): Flow<Result<Unit>>

    fun getBooksList(scope: CoroutineScope): Flow<Result<List<BookData>>>

    fun getBooksListByQuery(query: String): Flow<Result<List<BookData>>>

    fun getBookData(bookId: Int): Flow<BookData>

    fun getUiModeStatus(): Flow<Boolean>

    fun setUiModeStatus(): Flow<Boolean>

    fun saveCurrentBookPage(bookId: Int, currentPage: Int, pageCount: Int): Flow<Unit>

}