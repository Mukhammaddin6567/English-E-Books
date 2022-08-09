package uz.gita.bookapp.presentation.viewmodel.details

import androidx.lifecycle.LiveData
import uz.gita.bookapp.data.model.BookData

interface DetailsVM {

    val bookDataLD: LiveData<BookData>
    val popBackStackLD: LiveData<Unit>
    val showPermissionLD: LiveData<Unit>
    val navigateReadScreenLD: LiveData<Int>
    val errorMessageLD: LiveData<Int>

    fun onClickBackButton()
    fun loadData(bookId: Int)
    fun onClickReadBook()
    fun onPermissionSucceed(bookId:Int)
    fun onPermissionDenied()
    fun onResume(bookId: Int)
}