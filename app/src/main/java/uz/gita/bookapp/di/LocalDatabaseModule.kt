package uz.gita.bookapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.bookapp.data.local.dao.BookDao
import uz.gita.bookapp.data.local.database.BookDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @[Provides Singleton]
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase =
        BookDatabase.getDatabase(context)

    @[Provides Singleton]
    fun provideBookDao(database: BookDatabase): BookDao = database.bookDao()

}