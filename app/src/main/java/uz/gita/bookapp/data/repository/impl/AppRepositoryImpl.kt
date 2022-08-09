package uz.gita.bookapp.data.repository.impl

import uz.gita.bookapp.R
import uz.gita.bookapp.data.local.AppSharedPreferences
import uz.gita.bookapp.data.model.IntroData
import uz.gita.bookapp.data.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences
) : AppRepository {

    override fun introData(): List<IntroData> = listOf(
        IntroData(
            id = 1,
            image = R.drawable.intro1,
            title = R.string.text_intro_title1,
            description = R.string.text_intro_description1
        ),
        IntroData(
            id = 2,
            image = R.drawable.intro2,
            title = R.string.text_intro_title2,
            description = R.string.text_intro_description2
        ),
        IntroData(
            id = 3,
            image = R.drawable.intro3,
            title = R.string.text_intro_title3,
            description = R.string.text_intro_description3
        )
    )

    override fun checkForFistLaunch(): Boolean = appSharedPreferences.isFirstLaunch

    override fun dismissFirstLaunch() = kotlin.run { appSharedPreferences.isFirstLaunch = false }

    override suspend fun getUiModeStatus(): Boolean = appSharedPreferences.uiMode

}