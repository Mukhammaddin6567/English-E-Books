package uz.gita.bookapp.domain.usecase.intro

import uz.gita.bookapp.data.repository.AppRepository
import uz.gita.bookapp.domain.usecase.intro.IntroUC
import javax.inject.Inject

class IntroUCImpl
@Inject constructor(
    private val repository: AppRepository
) : IntroUC {

    override fun introData() = repository.introData()

    override fun dismissFirstLaunch() = kotlin.run { repository.dismissFirstLaunch() }

}