package uz.gita.bookapp.presentation.viewmodel.intro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import uz.gita.bookapp.data.model.IntroData
import uz.gita.bookapp.domain.usecase.intro.IntroUC
import uz.gita.bookapp.presentation.viewmodel.intro.IntroVM
import javax.inject.Inject

@HiltViewModel
class IntroVMImpl
@Inject constructor(
    private val useCase: IntroUC
) : ViewModel(), IntroVM {

    override val introDataLD: MutableLiveData<List<IntroData>> by lazy { MutableLiveData<List<IntroData>>() }
    override val navigateNextScreenLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val openNextPageLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val isLastPageLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    init {
        introDataLD.value = useCase.introData()
        isLastPageLD.value = false
    }

    override fun onClickNext(selectedTabPosition: Int) {
        Timber.d("selectedTabPosition: $selectedTabPosition")
        if (selectedTabPosition + 1 == (introDataLD.value?.size ?: 1)) {
            useCase.dismissFirstLaunch()
            navigateNextScreenLD.value = Unit
            return
        }
        openNextPageLD.value = Unit
    }

    override fun setSelectedPagePosition(currentPosition: Int) {
        isLastPageLD.value = (introDataLD.value?.size ?: 1) - 1 == currentPosition
    }

}