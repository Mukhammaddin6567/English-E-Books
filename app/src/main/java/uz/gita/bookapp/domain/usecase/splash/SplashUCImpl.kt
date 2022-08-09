package uz.gita.bookapp.domain.usecase.splash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.bookapp.data.repository.AppRepository
import javax.inject.Inject

class SplashUCImpl @Inject constructor(
    private val appRepository: AppRepository
) : SplashUC {

    override fun getUiModeStatus() = flow<Boolean> {
        emit(appRepository.getUiModeStatus())
    }.flowOn(Dispatchers.IO)

    override fun navigateNextScreen() = flow<Int> {
        when (appRepository.checkForFistLaunch()) {
            false -> emit(0)
            else -> emit(1)
        }
    }.flowOn(Dispatchers.IO)

}