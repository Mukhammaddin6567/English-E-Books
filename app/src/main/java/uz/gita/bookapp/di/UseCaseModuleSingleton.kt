package uz.gita.bookapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.bookapp.domain.usecase.books.BooksUC
import uz.gita.bookapp.domain.usecase.books.BooksUCImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModuleSingleton {

    @[Binds]
    fun bindBookUC(impl: BooksUCImpl): BooksUC

}