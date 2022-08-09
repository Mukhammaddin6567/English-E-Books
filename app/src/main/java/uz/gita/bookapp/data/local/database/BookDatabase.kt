package uz.gita.bookapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.bookapp.data.local.dao.BookDao
import uz.gita.bookapp.data.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        private lateinit var instance: BookDatabase

        fun getDatabase(context: Context): BookDatabase {
            if (::instance.isInitialized) return instance
            instance = Room
                .databaseBuilder(context, BookDatabase::class.java, context.packageName)
                .fallbackToDestructiveMigration()
                .build()
            return instance
        }
    }

}