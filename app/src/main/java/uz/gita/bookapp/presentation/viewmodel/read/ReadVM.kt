package uz.gita.bookapp.presentation.viewmodel.read

import android.content.Context
import androidx.lifecycle.LiveData
import uz.gita.bookapp.data.model.BookData

interface ReadVM {

    val bookDataLD: LiveData<BookData>
    val bookReferenceAndPageLD: LiveData<Pair<String, Int>>
    val networkErrorLD: LiveData<String>
    val localErrorLD: LiveData<Int>
    /*val showShareDialogLD: LiveData<String>*/
    val popBackStackLD: LiveData<Unit>
    val progressStatusLD: LiveData<Boolean>

    fun onClickBack()
    /*fun onClickShare()*/
    fun getBook(bookId: Int)
    fun loadBookByReference(context: Context, bookReference: String, currentPage: Int)
    fun onRefresh(bookId: Int)
    fun dismissProgress()
    fun saveCurrentBook(bookId: Int, currentPage: Int, pageCount: Int)

}