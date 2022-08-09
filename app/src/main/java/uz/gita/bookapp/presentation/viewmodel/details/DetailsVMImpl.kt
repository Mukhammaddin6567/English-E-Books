package uz.gita.bookapp.presentation.viewmodel.details

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
class DetailsVMImpl @Inject constructor(
    private val booksUC: BooksUC
) : ViewModel(), DetailsVM {

    override val bookDataLD: MutableLiveData<BookData> by lazy { MutableLiveData<BookData>() }
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val showPermissionLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val navigateReadScreenLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val errorMessageLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    override fun onClickBackButton() {
        popBackStackLD.value = Unit
    }

    override fun loadData(bookId: Int) {
        loadBookData(bookId)
    }

    override fun onClickReadBook() {
        showPermissionLD.value = Unit
    }

    override fun onPermissionSucceed(bookId: Int) {
        navigateReadScreenLD.value = bookId
    }

    override fun onPermissionDenied() {
//        errorMessageLD.value = R.string.text_error_permission_denied
    }

    override fun onResume(bookId: Int) {
        if (bookDataLD.value == null) return
        loadBookData(bookId)
    }

    private fun loadBookData(bookId: Int){
        booksUC
            .getBookData(bookId)
            .onEach { bookDataLD.value = it }
            .launchIn(viewModelScope)
    }

}