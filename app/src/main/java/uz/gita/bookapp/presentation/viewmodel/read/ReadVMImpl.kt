package uz.gita.bookapp.presentation.viewmodel.read

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import uz.gita.bookapp.data.model.BookData
import uz.gita.bookapp.domain.usecase.books.BooksUC
import javax.inject.Inject

@HiltViewModel
class ReadVMImpl
@Inject constructor(
    private val booksUC: BooksUC
) : ViewModel(), ReadVM {

    override val bookDataLD: MutableLiveData<BookData> by lazy { MutableLiveData<BookData>() }
    override val bookReferenceAndPageLD: MutableLiveData<Pair<String, Int>> by lazy { MutableLiveData<Pair<String, Int>>() }
    override val networkErrorLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val localErrorLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    /*override val showShareDialogLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }*/
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val progressStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    /*private var bookReference = ""*/

    override fun onClickBack() {
        popBackStackLD.value = Unit
    }

    /*override fun onClickShare() {
        showShareDialogLD.value = bookReference
    }*/

    override fun getBook(bookId: Int) {
        getBookData(bookId)
    }

    override fun loadBookByReference(context: Context, bookReference: String, currentPage: Int) {
        /*this.bookReference = bookReference*/
        progressStatusLD.value = true
        booksUC
            .loadBookByReference(context, bookReference)
            .onEach { result ->
                progressStatusLD.value = false
                result.onSuccess { bookReferenceAndPageLD.value = Pair(bookReference, currentPage) }
                result.onFailure { networkErrorLD.value = it.message }
            }
            .launchIn(viewModelScope)
    }

    override fun onRefresh(bookId: Int) {
        progressStatusLD.value = false
        return
//        getBookData(bookId)
    }

    override fun dismissProgress() {
        progressStatusLD.value = false
    }

    override fun saveCurrentBook(bookId: Int, currentPage: Int, pageCount: Int) {
        booksUC
            .saveCurrentBookPage(bookId, currentPage, pageCount)
            .launchIn(viewModelScope)
    }

    private fun getBookData(bookId: Int) {
        progressStatusLD.value = true
        Timber.d("getBook: $bookId")
        booksUC
            .getBookData(bookId)
            .onEach {
                progressStatusLD.value = false
                bookDataLD.value = it
            }
            .launchIn(viewModelScope)
    }
}