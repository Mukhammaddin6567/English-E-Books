package uz.gita.bookapp.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.bookapp.utils.SharedPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPreferences
@Inject constructor(@ApplicationContext context: Context) : SharedPreference(context) {

    var uiMode: Boolean by BooleanPreference(false)
    var isFirstLaunch: Boolean by BooleanPreference(true)

}