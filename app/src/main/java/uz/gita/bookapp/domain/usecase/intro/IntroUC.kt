package uz.gita.bookapp.domain.usecase.intro

import uz.gita.bookapp.data.model.IntroData

interface IntroUC {

    fun introData(): List<IntroData>
    fun dismissFirstLaunch()

}