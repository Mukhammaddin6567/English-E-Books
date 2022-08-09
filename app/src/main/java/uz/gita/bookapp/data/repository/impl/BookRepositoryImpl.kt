package uz.gita.bookapp.data.repository.impl

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import uz.gita.bookapp.BuildConfig.BOOK_APP_BOOKS
import uz.gita.bookapp.BuildConfig.BOOK_STORAGE
import uz.gita.bookapp.data.local.AppSharedPreferences
import uz.gita.bookapp.data.local.dao.BookDao
import uz.gita.bookapp.data.local.entity.BookEntity
import uz.gita.bookapp.data.remote.response.BookDataResponse
import uz.gita.bookapp.data.repository.BookRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl
@Inject constructor(
    private val store: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val bookDao: BookDao,
    private val appSharedPreferences: AppSharedPreferences
) : BookRepository {

    override suspend fun loadAllBooks(
        success: (List<BookDataResponse>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        store.collection(BOOK_APP_BOOKS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val response = querySnapshot.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(BookDataResponse::class.java)
                }
                success.invoke(response)
            }
            .addOnFailureListener { failure.invoke(it) }
    }

    override suspend fun loadBookByReference(
        context: Context,
        bookReference: String,
        success: () -> Unit,
        failure: (Throwable) -> Unit
    ) {
        storage
            .reference
            .child(BOOK_STORAGE)
            .child(bookReference)
            .getFile(File(context.filesDir, bookReference))
            .addOnSuccessListener {
                Timber.d("loadBookByReference: success")
                success.invoke()
            }
            .addOnFailureListener {
                Timber.d("loadBookByReference failure: ${it.message}")
                failure.invoke(it)
            }
    }

    override suspend fun getAllBooksByQuery(query: String): List<BookEntity> {
        Timber.d("getAllBooksByQuery: $query")
        return bookDao.getAllBooksByQuery("%${query}%")
    }

    override suspend fun saveAllBooks(books: List<BookEntity>) = bookDao.saveAllBooks(books)

    override suspend fun getAllBooks(): List<BookEntity> = bookDao.getAllBooks()

    override suspend fun getBookData(bookId: Int): BookEntity = bookDao.getBook(bookId)

    override suspend fun checkForAvailableBooks(): Boolean = bookDao.isAvailableBooks()

    override suspend fun saveCurrentBookPage(bookId: Int, currentPage: Int, pageCount: Int) {
        bookDao.saveCurrentPage(bookId, currentPage)
        if (bookDao.getPagesCountBookById(bookId) == 0)
            bookDao.savePagesCount(bookId, pageCount)
    }

    override suspend fun getUiModeStatus(): Boolean = appSharedPreferences.uiMode

    override suspend fun setUiModeStatus(): Boolean {
        val status = !appSharedPreferences.uiMode
        appSharedPreferences.uiMode = status
        Timber.d("setUiModeStatus: $status")
        return status
    }

}