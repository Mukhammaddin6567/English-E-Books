package uz.gita.bookapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.gita.bookapp.data.local.entity.BookEntity

@Dao
interface BookDao {

    @Query("select exists (select * from books)")
    suspend fun isAvailableBooks(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllBooks(books: List<BookEntity>)

    @Query("select * from books order by name asc")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("select * from books where name like :query or author like :query")
    suspend fun getAllBooksByQuery(query: String): List<BookEntity>

    @Query("select * from books where books.id =:bookId")
    suspend fun getBook(bookId: Int): BookEntity

    @Query("update books set currentPage=:currentPage where id=:bookId")
    suspend fun saveCurrentPage(bookId: Int, currentPage: Int)

    @Query("update books set pagesCount=:pagesCount where id=:bookId")
    suspend fun savePagesCount(bookId: Int, pagesCount: Int)

    @Query("select currentPage from books where id=:bookId")
    suspend fun getCurrentPageBookById(bookId: Int): Int

    @Query("select pagesCount from books where id=:bookId")
    suspend fun getPagesCountBookById(bookId: Int): Int

}