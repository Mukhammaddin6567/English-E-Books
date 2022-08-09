package uz.gita.bookapp.presentation.viewmodel.splash

import androidx.lifecycle.LiveData

interface SplashVM {

    val uiModeStatusLD: LiveData<Boolean>
    val versionLD: LiveData<String>
    val navigateBooksScreeLD: LiveData<Unit>
    val navigateIntroScreenLD: LiveData<Unit>

}