package uz.gita.bookapp.presentation.viewmodel.intro

import androidx.lifecycle.LiveData
import uz.gita.bookapp.data.model.IntroData

interface IntroVM {

    val introDataLD: LiveData<List<IntroData>>
    val navigateNextScreenLD: LiveData<Unit>
    val openNextPageLD: LiveData<Unit>
    val isLastPageLD: LiveData<Boolean>

    fun onClickNext(selectedTabPosition: Int)
    fun setSelectedPagePosition(currentPosition: Int)

}