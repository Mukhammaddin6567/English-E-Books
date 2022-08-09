package uz.gita.bookapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.bookapp.domain.usecase.intro.IntroUC
import uz.gita.bookapp.domain.usecase.intro.IntroUCImpl
import uz.gita.bookapp.domain.usecase.splash.SplashUC
import uz.gita.bookapp.domain.usecase.splash.SplashUCImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @[Binds]
    fun bindSplashUseCase(impl: SplashUCImpl): SplashUC

    @[Binds]
    fun bindIntroUseCase(impl: IntroUCImpl): IntroUC

}