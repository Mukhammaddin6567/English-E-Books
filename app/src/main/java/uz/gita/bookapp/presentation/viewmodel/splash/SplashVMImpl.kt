package uz.gita.bookapp.presentation.viewmodel.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.bookapp.BuildConfig
import uz.gita.bookapp.domain.usecase.splash.SplashUC
import javax.inject.Inject

@HiltViewModel
class SplashVMImpl @Inject constructor(
    private val splashUC: SplashUC
) : ViewModel(), SplashVM {

    override val uiModeStatusLD: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    override val versionLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    override val navigateBooksScreeLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    override val navigateIntroScreenLD: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    init {
        uiModeStatus()
        version()
        navigateNextScreen()
    }

    private fun uiModeStatus() {
        splashUC
            .getUiModeStatus()
            .onEach { uiModeStatusLD.value = it }
            .launchIn(viewModelScope)
    }

    private fun version() {
        versionLD.value = BuildConfig.VERSION_NAME
    }

    private fun navigateNextScreen() {
        splashUC
            .navigateNextScreen()
            .onEach { result ->
                delay(2000)
                when (result) {
                    0 -> navigateBooksScreeLD.value = Unit
                    else -> navigateIntroScreenLD.value = Unit
                }
            }
            .launchIn(viewModelScope)
    }
}