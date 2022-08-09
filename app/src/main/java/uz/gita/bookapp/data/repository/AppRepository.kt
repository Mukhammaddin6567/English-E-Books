package uz.gita.bookapp.data.repository

import uz.gita.bookapp.data.model.IntroData

interface AppRepository {

    // intro screen
    fun introData(): List<IntroData>
    fun checkForFistLaunch(): Boolean
    fun dismissFirstLaunch()

    // splash screen
    suspend fun getUiModeStatus(): Boolean

}