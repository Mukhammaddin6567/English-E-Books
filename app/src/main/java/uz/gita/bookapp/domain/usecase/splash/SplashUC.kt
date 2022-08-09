package uz.gita.bookapp.domain.usecase.splash

import kotlinx.coroutines.flow.Flow

interface SplashUC {

    fun getUiModeStatus(): Flow<Boolean>
    fun navigateNextScreen(): Flow<Int>

}