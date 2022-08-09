package uz.gita.bookapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.bookapp.data.repository.AppRepository
import uz.gita.bookapp.data.repository.BookRepository
import uz.gita.bookapp.data.repository.impl.AppRepositoryImpl
import uz.gita.bookapp.data.repository.impl.BookRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModuleSingleton {

    @[Binds Singleton]
    fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

}