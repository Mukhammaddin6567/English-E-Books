package uz.gita.bookapp.presentation.viewmodel.books

import androidx.lifecycle.LiveData
import uz.gita.bookapp.data.model.BookData

interface BooksVM {

    val placeholderListStatusLD: LiveData<Boolean>
    val placeholderSearchLD: LiveData<Boolean>
    val closeSearchLD: LiveData<Unit>
    val refreshStatusLD: LiveData<Boolean>
    val navigateDetailsScreenLD: LiveData<Int>
    val showBottomDialogLD: LiveData<Boolean>
    val showShareDialogLD: LiveData<Pair<Int, Int>>
    val showRateScreenLD: LiveData<Int>
    val showAboutDialogLD: LiveData<Unit>
    val hideKeyboardLD: LiveData<Unit>
    val intErrorLD: LiveData<Int>
    val stringErrorLD: LiveData<String>
    val booksListLD: LiveData<List<BookData>>
    val uiModeStatusLD: LiveData<Boolean>
    val popBackStackLD: LiveData<Unit>

    fun networkAvailable()
    fun onClickBookItem(bookId: Int)
    fun onClickMoreButton()
    fun onSwipe()
    fun onResume()
    fun onClickUpdateButton()
    fun onClickShareButton()
    fun onClickRateButton()
    fun onClickUiModeButton()
    fun onClickAboutButton()
    fun onClickQuitAppButton()
    fun onSearch(query: String)
    fun onClickClose()

}