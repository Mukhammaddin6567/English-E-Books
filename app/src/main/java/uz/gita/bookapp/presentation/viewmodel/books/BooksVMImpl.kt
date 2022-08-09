package uz.gita.bookapp.presentation.viewmodel.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import uz.gita.bookapp.R
import uz.gita.bookapp.data.model.BookData
import uz.gita.bookapp.domain.usecase.books.BooksUC
import javax.inject.Inject

@HiltViewModel
class BooksVMImpl
@Inject constructor(
    private val booksUC: BooksUC
) : ViewModel(), BooksVM {

    override val placeholderListStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val placeholderSearchLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val closeSearchLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val refreshStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val navigateDetailsScreenLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val showBottomDialogLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val showShareDialogLD: MutableLiveData<Pair<Int, Int>> by lazy { MutableLiveData<Pair<Int, Int>>() }
    override val showRateScreenLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val showAboutDialogLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val hideKeyboardLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val intErrorLD: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    override val stringErrorLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val booksListLD: MutableLiveData<List<BookData>> by lazy { MutableLiveData<List<BookData>>() }
    override val uiModeStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val popBackStackLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    private var job: Job? = null

    init {
        getAllBooks()
    }

    override fun networkAvailable() {
        Timber.d("networkAvailable")
        getAllBooks()
    }

    override fun onClickBookItem(bookId: Int) {
        navigateDetailsScreenLD.value = bookId
    }

    override fun onClickMoreButton() {
        booksUC
            .getUiModeStatus()
            .onEach { showBottomDialogLD.value = it }
            .launchIn(viewModelScope)
    }

    override fun onSwipe() {
        closeSearchLD.value = Unit
        hideKeyboardLD.value = Unit
        getAllBooks()
    }

    override fun onResume() {
        Timber.d("onResume")
        if (booksListLD.value == null) return
        getAllBooks()
    }

    override fun onClickUpdateButton() {
        loadAllBooks()
    }

    override fun onClickShareButton() {
        showShareDialogLD.value = Pair(R.string.text_share_details, R.string.text_share_title)
    }

    override fun onClickRateButton() {
        showRateScreenLD.value = R.string.text_rate_uri
    }

    override fun onClickUiModeButton() {
        booksUC
            .setUiModeStatus()
            .onEach { uiModeStatusLD.value = it }
            .launchIn(viewModelScope)
    }

    override fun onClickAboutButton() {
        showAboutDialogLD.value = Unit
    }

    override fun onClickQuitAppButton() {
        popBackStackLD.value = Unit
    }

    override fun onSearch(query: String) {
        job?.cancel()
        Timber.d("onSearch: $query")
        if (query.isEmpty()) {
            getAllBooks()
            return
        }
        refreshStatusLD.value = true
        job = booksUC
            .getBooksListByQuery(query)
            .onEach { result ->
                refreshStatusLD.value = false
                result.onSuccess { list ->
                    placeholderSearchLD.value = false
                    booksListLD.value = list
                }
                result.onFailure { placeholderSearchLD.value = true }
            }
            .launchIn(viewModelScope)
    }

    override fun onClickClose() {
        closeSearchLD.value = Unit
        getAllBooks()
    }

    private fun getAllBooks() {
        refreshStatusLD.value = true
        placeholderSearchLD.value = false
        booksUC
            .getBooksList(viewModelScope)
            .onEach { result ->
                refreshStatusLD.value = false
                result.onSuccess { list ->
                    placeholderListStatusLD.value = list.isEmpty()
                    booksListLD.value = list
                }
                result.onFailure { throwable ->
                    placeholderListStatusLD.value = true
                    stringErrorLD.value = throwable.message
                }
            }.launchIn(viewModelScope)
    }

    private fun loadAllBooks() {
        refreshStatusLD.value = true
        placeholderSearchLD.value = false
        booksUC
            .loadBooksList(viewModelScope)
            .onEach { result ->
                refreshStatusLD.value = false
                result.onSuccess { list ->
                    placeholderListStatusLD.value = list.isEmpty()
                    booksListLD.value = list
                }
                result.onFailure { throwable ->
                    placeholderListStatusLD.value = true
                    stringErrorLD.value = throwable.message
                }
            }.launchIn(viewModelScope)
    }

}