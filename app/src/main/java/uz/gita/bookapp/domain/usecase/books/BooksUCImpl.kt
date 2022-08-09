package uz.gita.bookapp.domain.usecase.books

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import uz.gita.bookapp.data.model.BookData
import uz.gita.bookapp.data.repository.BookRepository
import java.io.File
import javax.inject.Inject

class BooksUCImpl
@Inject constructor(
    private val repository: BookRepository
) : BooksUC {

    override fun loadBooksList(scope: CoroutineScope) = callbackFlow<Result<List<BookData>>> {
        repository.loadAllBooks(
            { list ->
                val saveBooks = list.map { it.toBookEntity() }
                scope.launch(Dispatchers.IO) { repository.saveAllBooks(saveBooks) }
                val result = list.map { it.toBookData() }
                trySendBlocking(Result.success(result))
            },
            { trySendBlocking(Result.failure(it)) }
        )
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun loadBookByReference(context: Context, bookReference: String) =
        callbackFlow<Result<Unit>> {
            if (File(
                    context.filesDir,
                    bookReference
                ).exists()
            ) trySendBlocking(Result.success(Unit))
            else {
                repository.loadBookByReference(
                    context,
                    bookReference,
                    { trySendBlocking(Result.success(Unit)) },
                    { trySendBlocking(Result.failure(it)) }
                )
            }
            awaitClose {}
        }.flowOn(Dispatchers.IO)

    override fun getBooksList(scope: CoroutineScope) = callbackFlow<Result<List<BookData>>> {
        if (repository.checkForAvailableBooks()) {
            val result = repository.getAllBooks().map { it.toBookData() }
            trySendBlocking(Result.success(result))
        } else {
            repository.loadAllBooks(
                { list ->
                    val saveBooks = list.map { it.toBookEntity() }
                    scope.launch(Dispatchers.IO) { repository.saveAllBooks(saveBooks) }
                    val result = list.map { it.toBookData() }
                    trySendBlocking(Result.success(result))
                },
                { trySendBlocking(Result.failure(it)) }
            )
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override fun getBooksListByQuery(query: String) = flow<Result<List<BookData>>> {
        delay(500)
        val result = repository.getAllBooksByQuery(query)
        if (result.isNotEmpty()) emit(Result.success(result.map { it.toBookData() }))
        else emit(Result.failure(Exception()))
    }.flowOn(Dispatchers.IO)

    override fun getBookData(bookId: Int) = flow<BookData> {
        val result = repository.getBookData(bookId)
        emit(result.toBookData())
    }.flowOn(Dispatchers.IO)

    override fun getUiModeStatus() = flow<Boolean> {
        emit(repository.getUiModeStatus())
    }.flowOn(Dispatchers.IO)

    override fun setUiModeStatus() = flow<Boolean> {
        emit(repository.setUiModeStatus())
    }.flowOn(Dispatchers.IO)

    override fun saveCurrentBookPage(bookId: Int, currentPage: Int, pageCount: Int) = flow<Unit> {
        repository.saveCurrentBookPage(bookId, currentPage, pageCount)
    }.flowOn(Dispatchers.IO)

}